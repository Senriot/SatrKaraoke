/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer.extractor.mp4;

import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.extractor.mp4.Atom.ContainerAtom;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableByteArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Extracts data from an unfragmented MP4 file.
 */
public final class Mp4Extractor implements Extractor,
                                           SeekMap
{

    // Parser states.
    private static final int STATE_AFTER_SEEK           = 0;
    private static final int STATE_READING_ATOM_HEADER  = 1;
    private static final int STATE_READING_ATOM_PAYLOAD = 2;
    private static final int STATE_READING_SAMPLE       = 3;

    /**
     * When seeking within the source, if the offset is greater than or equal to this value (or the
     * offset is negative), the source will be reloaded.
     */
    private static final long RELOAD_MINIMUM_SEEK_DISTANCE = 256 * 1024;

    // Temporary arrays.
    private final ParsableByteArray nalStartCode;
    private final ParsableByteArray nalLength;

    private final ParsableByteArray    atomHeader;
    private final Stack<ContainerAtom> containerAtoms;

    private int               parserState;
    private int               atomType;
    private long              atomSize;
    private int               atomHeaderBytesRead;
    private ParsableByteArray atomData;

    private int sampleSize;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;

    // Extractor outputs.
    private ExtractorOutput extractorOutput;
    private Mp4Track[]      tracks;

    public Mp4Extractor()
    {
        atomHeader = new ParsableByteArray(Atom.LONG_HEADER_SIZE);
        containerAtoms = new Stack<>();
        nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        nalLength = new ParsableByteArray(4);
        enterReadingAtomHeaderState();
    }

    @Override
    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException
    {
        return Sniffer.sniffUnfragmented(input);
    }

    @Override
    public void init(ExtractorOutput output)
    {
        extractorOutput = output;
    }

    @Override
    public void seek()
    {
        containerAtoms.clear();
        atomHeaderBytesRead = 0;
        sampleBytesWritten = 0;
        sampleCurrentNalBytesRemaining = 0;
        parserState = STATE_AFTER_SEEK;
    }

    @Override
    public int read(ExtractorInput input, PositionHolder seekPosition)
            throws IOException, InterruptedException
    {
        while (true)
        {
            switch (parserState)
            {
                case STATE_AFTER_SEEK:
                    if (input.getPosition() == 0)
                    {
                        enterReadingAtomHeaderState();
                    }
                    else
                    {
                        parserState = STATE_READING_SAMPLE;
                    }
                    break;
                case STATE_READING_ATOM_HEADER:
                    if (!readAtomHeader(input))
                    {
                        return RESULT_END_OF_INPUT;
                    }
                    break;
                case STATE_READING_ATOM_PAYLOAD:
                    if (readAtomPayload(input, seekPosition))
                    {
                        return RESULT_SEEK;
                    }
                    break;
                default:
                    return readSample(input, seekPosition);
            }
        }
    }

    // SeekMap implementation.

    @Override
    public boolean isSeekable()
    {
        return true;
    }

    @Override
    public long getPosition(long timeUs)
    {
        long earliestSamplePosition = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < tracks.length; trackIndex++)
        {
            TrackSampleTable sampleTable = tracks[trackIndex].sampleTable;
            int sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(timeUs);
            if (sampleIndex == TrackSampleTable.NO_SAMPLE)
            {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(timeUs);
            }
            tracks[trackIndex].sampleIndex = sampleIndex;

            long offset = sampleTable.offsets[tracks[trackIndex].sampleIndex];
            if (offset < earliestSamplePosition)
            {
                earliestSamplePosition = offset;
            }
        }
        return earliestSamplePosition;
    }

    // Private methods.

    private void enterReadingAtomHeaderState()
    {
        parserState = STATE_READING_ATOM_HEADER;
        atomHeaderBytesRead = 0;
    }

    private boolean readAtomHeader(ExtractorInput input) throws IOException, InterruptedException
    {
        if (atomHeaderBytesRead == 0)
        {
            // Read the standard length atom header.
            if (!input.readFully(atomHeader.data, 0, Atom.HEADER_SIZE, true))
            {
                return false;
            }
            atomHeaderBytesRead = Atom.HEADER_SIZE;
            atomHeader.setPosition(0);
            atomSize = atomHeader.readUnsignedInt();
            atomType = atomHeader.readInt();
        }

        if (atomSize == Atom.LONG_SIZE_PREFIX)
        {
            // Read the extended atom size.
            int headerBytesRemaining = Atom.LONG_HEADER_SIZE - Atom.HEADER_SIZE;
            input.readFully(atomHeader.data, Atom.HEADER_SIZE, headerBytesRemaining);
            atomHeaderBytesRead += headerBytesRemaining;
            atomSize = atomHeader.readUnsignedLongToLong();
        }

        if (shouldParseContainerAtom(atomType))
        {
            long endPosition = input.getPosition() + atomSize - atomHeaderBytesRead;
            containerAtoms.add(new ContainerAtom(atomType, endPosition));
            enterReadingAtomHeaderState();
        }
        else if (shouldParseLeafAtom(atomType))
        {
            // We don't support parsing of leaf atoms that define extended atom sizes, or that have
            // lengths greater than Integer.MAX_VALUE.
            Assertions.checkState(atomHeaderBytesRead == Atom.HEADER_SIZE);
            Assertions.checkState(atomSize <= Integer.MAX_VALUE);
            atomData = new ParsableByteArray((int) atomSize);
            System.arraycopy(atomHeader.data, 0, atomData.data, 0, Atom.HEADER_SIZE);
            parserState = STATE_READING_ATOM_PAYLOAD;
        }
        else
        {
            atomData = null;
            parserState = STATE_READING_ATOM_PAYLOAD;
        }

        return true;
    }

    /**
     * Processes the atom payload. If {@link #atomData} is null and the size is at or above the
     * threshold {@link #RELOAD_MINIMUM_SEEK_DISTANCE}, {@code true} is returned and the caller should
     * restart loading at the position in {@code positionHolder}. Otherwise, the atom is read/skipped.
     */
    private boolean readAtomPayload(ExtractorInput input, PositionHolder positionHolder)
            throws IOException, InterruptedException
    {
        long    atomPayloadSize = atomSize - atomHeaderBytesRead;
        long    atomEndPosition = input.getPosition() + atomPayloadSize;
        boolean seekRequired    = false;
        if (atomData != null)
        {
            input.readFully(atomData.data, atomHeaderBytesRead, (int) atomPayloadSize);
            if (!containerAtoms.isEmpty())
            {
                containerAtoms.peek().add(new Atom.LeafAtom(atomType, atomData));
            }
        }
        else
        {
            // We don't need the data. Skip or seek, depending on how large the atom is.
            if (atomPayloadSize < RELOAD_MINIMUM_SEEK_DISTANCE)
            {
                input.skipFully((int) atomPayloadSize);
            }
            else
            {
                positionHolder.position = input.getPosition() + atomPayloadSize;
                seekRequired = true;
            }
        }

        while (!containerAtoms.isEmpty() && containerAtoms.peek().endPosition == atomEndPosition)
        {
            ContainerAtom containerAtom = containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov)
            {
                // We've reached the end of the moov atom. Process it and prepare to read samples.
                processMoovAtom(containerAtom);
                containerAtoms.clear();
                parserState = STATE_READING_SAMPLE;
                return false;
            }
            else if (!containerAtoms.isEmpty())
            {
                containerAtoms.peek().add(containerAtom);
            }
        }

        enterReadingAtomHeaderState();
        return seekRequired;
    }

    /**
     * Updates the stored track metadata to reflect the contents of the specified moov atom.
     */
    private void processMoovAtom(ContainerAtom moov)
    {
        List<Mp4Track> tracks               = new ArrayList<>();
        long           earliestSampleOffset = Long.MAX_VALUE;
        for (int i = 0; i < moov.containerChildren.size(); i++)
        {
            ContainerAtom atom = moov.containerChildren.get(i);
            if (atom.type != Atom.TYPE_trak)
            {
                continue;
            }

            Track track = AtomParsers.parseTrak(atom, moov.getLeafAtomOfType(Atom.TYPE_mvhd));
            if (track == null)
            {
                continue;
            }

            ContainerAtom stblAtom = atom.getContainerAtomOfType(Atom.TYPE_mdia)
                                         .getContainerAtomOfType(Atom.TYPE_minf)
                                         .getContainerAtomOfType(Atom.TYPE_stbl);
            TrackSampleTable trackSampleTable = AtomParsers.parseStbl(track, stblAtom);
            if (trackSampleTable.sampleCount == 0)
            {
                continue;
            }

            Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, extractorOutput.track(i));
            mp4Track.trackOutput.format(track.mediaFormat);
            tracks.add(mp4Track);

            long firstSampleOffset = trackSampleTable.offsets[0];
            if (firstSampleOffset < earliestSampleOffset)
            {
                earliestSampleOffset = firstSampleOffset;
            }
        }
        this.tracks = tracks.toArray(new Mp4Track[0]);
        extractorOutput.endTracks();
        extractorOutput.seekMap(this);
    }

    /**
     * Attempts to extract the next sample in the current mdat atom for the specified track.
     * <p/>
     * Returns {@link #RESULT_SEEK} if the source should be reloaded from the position in
     * {@code positionHolder}.
     * <p/>
     * Returns {@link #RESULT_END_OF_INPUT} if no samples are left. Otherwise, returns
     * {@link #RESULT_CONTINUE}.
     *
     * @param input          The {@link ExtractorInput} from which to read data.
     * @param positionHolder If {@link #RESULT_SEEK} is returned, this holder is updated to hold the
     *                       position of the required data.
     * @return One of the {@code RESULT_*} flags in {@link Extractor}.
     * @throws IOException          If an error occurs reading from the input.
     * @throws InterruptedException If the thread is interrupted.
     */
    private int readSample(ExtractorInput input, PositionHolder positionHolder)
            throws IOException, InterruptedException
    {
        int trackIndex = getTrackIndexOfEarliestCurrentSample();
        if (trackIndex == TrackSampleTable.NO_SAMPLE)
        {
            return RESULT_END_OF_INPUT;
        }
        Mp4Track    track       = tracks[trackIndex];
        TrackOutput trackOutput = track.trackOutput;
        int         sampleIndex = track.sampleIndex;
        long        position    = track.sampleTable.offsets[sampleIndex];
        long        skipAmount  = position - input.getPosition() + sampleBytesWritten;
        if (skipAmount < 0 || skipAmount >= RELOAD_MINIMUM_SEEK_DISTANCE)
        {
            positionHolder.position = position;
            return RESULT_SEEK;
        }
        input.skipFully((int) skipAmount);
        sampleSize = track.sampleTable.sizes[sampleIndex];
        if (track.track.nalUnitLengthFieldLength != -1)
        {
            // Zero the top three bytes of the array that we'll use to parse nal unit lengths, in case
            // they're only 1 or 2 bytes long.
            byte[] nalLengthData = nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.track.nalUnitLengthFieldLength;
            // NAL units are length delimited, but the decoder requires start code delimited units.
            // Loop until we've written the sample to the track output, replacing length delimiters with
            // start codes as we encounter them.
            while (sampleBytesWritten < sampleSize)
            {
                if (sampleCurrentNalBytesRemaining == 0)
                {
                    // Read the NAL length so that we know where we find the next one.
                    input.readFully(nalLength.data,
                                    nalUnitLengthFieldLengthDiff,
                                    nalUnitLengthFieldLength);
                    nalLength.setPosition(0);
                    sampleCurrentNalBytesRemaining = nalLength.readUnsignedIntToInt();
                    // Write a start code for the current NAL unit.
                    nalStartCode.setPosition(0);
                    trackOutput.sampleData(nalStartCode, 4);
                    sampleBytesWritten += 4;
                    sampleSize += nalUnitLengthFieldLengthDiff;
                }
                else
                {
                    // Write the payload of the NAL unit.
                    int writtenBytes = trackOutput.sampleData(input,
                                                              sampleCurrentNalBytesRemaining,
                                                              false);
                    sampleBytesWritten += writtenBytes;
                    sampleCurrentNalBytesRemaining -= writtenBytes;
                }
            }
        }
        else
        {
            while (sampleBytesWritten < sampleSize)
            {
                int writtenBytes = trackOutput.sampleData(input,
                                                          sampleSize - sampleBytesWritten,
                                                          false);
                sampleBytesWritten += writtenBytes;
                sampleCurrentNalBytesRemaining -= writtenBytes;
            }
        }
        trackOutput.sampleMetadata(track.sampleTable.timestampsUs[sampleIndex],
                                   track.sampleTable.flags[sampleIndex], sampleSize, 0, null);
        track.sampleIndex++;
        sampleBytesWritten = 0;
        sampleCurrentNalBytesRemaining = 0;
        return RESULT_CONTINUE;
    }

    /**
     * Returns the index of the track that contains the earliest current sample, or
     * {@link TrackSampleTable#NO_SAMPLE} if no samples remain.
     */
    private int getTrackIndexOfEarliestCurrentSample()
    {
        int  earliestSampleTrackIndex = TrackSampleTable.NO_SAMPLE;
        long earliestSampleOffset     = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < tracks.length; trackIndex++)
        {
            Mp4Track track = tracks[trackIndex];
            int sampleIndex = track.sampleIndex;
            if (sampleIndex == track.sampleTable.sampleCount)
            {
                continue;
            }

            long trackSampleOffset = track.sampleTable.offsets[sampleIndex];
            if (trackSampleOffset < earliestSampleOffset)
            {
                earliestSampleOffset = trackSampleOffset;
                earliestSampleTrackIndex = trackIndex;
            }
        }

        return earliestSampleTrackIndex;
    }

    /**
     * Returns whether the extractor should parse a leaf atom with type {@code atom}.
     */
    private static boolean shouldParseLeafAtom(int atom)
    {
        return atom == Atom.TYPE_mdhd || atom == Atom.TYPE_mvhd || atom == Atom.TYPE_hdlr
                || atom == Atom.TYPE_vmhd || atom == Atom.TYPE_smhd || atom == Atom.TYPE_stsd
                || atom == Atom.TYPE_avc1 || atom == Atom.TYPE_avcC || atom == Atom.TYPE_mp4a
                || atom == Atom.TYPE_esds || atom == Atom.TYPE_stts || atom == Atom.TYPE_stss
                || atom == Atom.TYPE_ctts || atom == Atom.TYPE_stsc || atom == Atom.TYPE_stsz
                || atom == Atom.TYPE_stco || atom == Atom.TYPE_co64 || atom == Atom.TYPE_tkhd
                || atom == Atom.TYPE_s263;
    }

    /**
     * Returns whether the extractor should parse a container atom with type {@code atom}.
     */
    private static boolean shouldParseContainerAtom(int atom)
    {
        return atom == Atom.TYPE_moov || atom == Atom.TYPE_trak || atom == Atom.TYPE_mdia
                || atom == Atom.TYPE_minf || atom == Atom.TYPE_stbl;
    }

    private static final class Mp4Track
    {

        public final Track            track;
        public final TrackSampleTable sampleTable;
        public final TrackOutput      trackOutput;

        public int sampleIndex;

        public Mp4Track(Track track, TrackSampleTable sampleTable, TrackOutput trackOutput)
        {
            this.track = track;
            this.sampleTable = sampleTable;
            this.trackOutput = trackOutput;
        }

    }

}
