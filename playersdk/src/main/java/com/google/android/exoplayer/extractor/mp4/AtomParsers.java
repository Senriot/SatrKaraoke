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

import com.google.android.exoplayer.C;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.Ac3Util;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.CodecSpecificDataUtil;
import com.google.android.exoplayer.util.MimeTypes;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.exoplayer.util.ParsableBitArray;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility methods for parsing MP4 format atom payloads according to ISO 14496-12.
 */
/* package */ final class AtomParsers
{

    /**
     * Parses a trak atom (defined in 14496-12).
     *
     * @param trak Atom to parse.
     * @param mvhd Movie header atom, used to get the timescale.
     * @return A {@link Track} instance, or {@code null} if the track's type isn't supported.
     */
    public static Track parseTrak(Atom.ContainerAtom trak, Atom.LeafAtom mvhd)
    {
        Atom.ContainerAtom mdia      = trak.getContainerAtomOfType(Atom.TYPE_mdia);
        int                trackType = parseHdlr(mdia.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (trackType != Track.TYPE_soun && trackType != Track.TYPE_vide && trackType != Track.TYPE_text
                && trackType != Track.TYPE_sbtl && trackType != Track.TYPE_subt)
        {
            return null;
        }

        TkhdData tkhdData       = parseTkhd(trak.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        long     duration       = tkhdData.duration;
        long     movieTimescale = parseMvhd(mvhd.data);
        long     durationUs;
        if (duration == -1)
        {
            durationUs = C.UNKNOWN_TIME_US;
        }
        else
        {
            durationUs = Util.scaleLargeTimestamp(duration, C.MICROS_PER_SECOND, movieTimescale);
        }
        Atom.ContainerAtom stbl = mdia.getContainerAtomOfType(Atom.TYPE_minf)
                                      .getContainerAtomOfType(Atom.TYPE_stbl);

        Pair<Long, String> mdhdData = parseMdhd(mdia.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData stsdData = parseStsd(stbl.getLeafAtomOfType(Atom.TYPE_stsd).data, durationUs,
                                      tkhdData.rotationDegrees, mdhdData.second);
        return stsdData.mediaFormat == null ? null
                : new Track(tkhdData.id,
                            trackType,
                            mdhdData.first,
                            durationUs,
                            stsdData.mediaFormat,
                            stsdData.trackEncryptionBoxes,
                            stsdData.nalUnitLengthFieldLength);
    }

    /**
     * Parses an stbl atom (defined in 14496-12).
     *
     * @param track    Track to which this sample table corresponds.
     * @param stblAtom stbl (sample table) atom to parse.
     * @return Sample table described by the stbl atom.
     */
    public static TrackSampleTable parseStbl(Track track, Atom.ContainerAtom stblAtom)
    {
        // Array of sample sizes.
        ParsableByteArray stsz = stblAtom.getLeafAtomOfType(Atom.TYPE_stsz).data;

        // Entries are byte offsets of chunks.
        ParsableByteArray chunkOffsets;
        Atom.LeafAtom     chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stco);
        if (chunkOffsetsAtom == null)
        {
            chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_co64);
        }
        chunkOffsets = chunkOffsetsAtom.data;
        // Entries are (chunk number, number of samples per chunk, sample description index).
        ParsableByteArray stsc = stblAtom.getLeafAtomOfType(Atom.TYPE_stsc).data;
        // Entries are (number of samples, timestamp delta between those samples).
        ParsableByteArray stts = stblAtom.getLeafAtomOfType(Atom.TYPE_stts).data;
        // Entries are the indices of samples that are synchronization samples.
        Atom.LeafAtom     stssAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray stss     = stssAtom != null ? stssAtom.data : null;
        // Entries are (number of samples, timestamp offset).
        Atom.LeafAtom     cttsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray ctts     = cttsAtom != null ? cttsAtom.data : null;

        // Skip full atom.
        stsz.setPosition(Atom.FULL_HEADER_SIZE);
        int fixedSampleSize = stsz.readUnsignedIntToInt();
        int sampleCount     = stsz.readUnsignedIntToInt();

        long[] offsets    = new long[sampleCount];
        int[]  sizes      = new int[sampleCount];
        long[] timestamps = new long[sampleCount];
        int[]  flags      = new int[sampleCount];
        if (sampleCount == 0)
        {
            return new TrackSampleTable(offsets, sizes, timestamps, flags);
        }

        // Prepare to read chunk offsets.
        chunkOffsets.setPosition(Atom.FULL_HEADER_SIZE);
        int chunkCount = chunkOffsets.readUnsignedIntToInt();

        stsc.setPosition(Atom.FULL_HEADER_SIZE);
        int remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt() - 1;
        Assertions.checkState(stsc.readInt() == 1, "stsc first chunk must be 1");
        int samplesPerChunk = stsc.readUnsignedIntToInt();
        stsc.skipBytes(4); // Skip the sample description index.
        int nextSamplesPerChunkChangeChunkIndex = -1;
        if (remainingSamplesPerChunkChanges > 0)
        {
            // Store the chunk index when the samples-per-chunk will next change.
            nextSamplesPerChunkChangeChunkIndex = stsc.readUnsignedIntToInt() - 1;
        }

        int chunkIndex              = 0;
        int remainingSamplesInChunk = samplesPerChunk;

        // Prepare to read sample timestamps.
        stts.setPosition(Atom.FULL_HEADER_SIZE);
        int remainingTimestampDeltaChanges   = stts.readUnsignedIntToInt() - 1;
        int remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
        int timestampDeltaInTimeUnits        = stts.readUnsignedIntToInt();

        // Prepare to read sample timestamp offsets, if ctts is present.
        int remainingSamplesAtTimestampOffset = 0;
        int remainingTimestampOffsetChanges   = 0;
        int timestampOffset                   = 0;
        if (ctts != null)
        {
            ctts.setPosition(Atom.FULL_HEADER_SIZE);
            remainingTimestampOffsetChanges = ctts.readUnsignedIntToInt() - 1;
            remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
            // The BMFF spec (ISO 14496-12) states that sample offsets should be unsigned integers in
            // version 0 ctts boxes, however some streams violate the spec and use signed integers
            // instead. It's safe to always parse sample offsets as signed integers here, because
            // unsigned integers will still be parsed correctly (unless their top bit is set, which
            // is never true in practice because sample offsets are always small).
            timestampOffset = ctts.readInt();
        }

        int nextSynchronizationSampleIndex  = -1;
        int remainingSynchronizationSamples = 0;
        if (stss != null)
        {
            stss.setPosition(Atom.FULL_HEADER_SIZE);
            remainingSynchronizationSamples = stss.readUnsignedIntToInt();
            nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
        }

        // Calculate the chunk offsets
        long offsetBytes;
        if (chunkOffsetsAtom.type == Atom.TYPE_stco)
        {
            offsetBytes = chunkOffsets.readUnsignedInt();
        }
        else
        {
            offsetBytes = chunkOffsets.readUnsignedLongToLong();
        }

        long timestampTimeUnits = 0;
        for (int i = 0; i < sampleCount; i++)
        {
            offsets[i] = offsetBytes;
            sizes[i] = fixedSampleSize == 0 ? stsz.readUnsignedIntToInt() : fixedSampleSize;
            timestamps[i] = timestampTimeUnits + timestampOffset;

            // All samples are synchronization samples if the stss is not present.
            flags[i] = stss == null ? C.SAMPLE_FLAG_SYNC : 0;
            if (i == nextSynchronizationSampleIndex)
            {
                flags[i] = C.SAMPLE_FLAG_SYNC;
                remainingSynchronizationSamples--;
                if (remainingSynchronizationSamples > 0)
                {
                    nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
                }
            }

            // Add on the duration of this sample.
            timestampTimeUnits += timestampDeltaInTimeUnits;
            remainingSamplesAtTimestampDelta--;
            if (remainingSamplesAtTimestampDelta == 0 && remainingTimestampDeltaChanges > 0)
            {
                remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
                timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
                remainingTimestampDeltaChanges--;
            }

            // Add on the timestamp offset if ctts is present.
            if (ctts != null)
            {
                remainingSamplesAtTimestampOffset--;
                if (remainingSamplesAtTimestampOffset == 0 && remainingTimestampOffsetChanges > 0)
                {
                    remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
                    // Read a signed offset even for version 0 ctts boxes (see comment above).
                    timestampOffset = ctts.readInt();
                    remainingTimestampOffsetChanges--;
                }
            }

            // If we're at the last sample in this chunk, move to the next chunk.
            remainingSamplesInChunk--;
            if (remainingSamplesInChunk == 0)
            {
                chunkIndex++;
                if (chunkIndex < chunkCount)
                {
                    if (chunkOffsetsAtom.type == Atom.TYPE_stco)
                    {
                        offsetBytes = chunkOffsets.readUnsignedInt();
                    }
                    else
                    {
                        offsetBytes = chunkOffsets.readUnsignedLongToLong();
                    }
                }

                // Change the samples-per-chunk if required.
                if (chunkIndex == nextSamplesPerChunkChangeChunkIndex)
                {
                    samplesPerChunk = stsc.readUnsignedIntToInt();
                    stsc.skipBytes(4); // Skip the sample description index.
                    remainingSamplesPerChunkChanges--;
                    if (remainingSamplesPerChunkChanges > 0)
                    {
                        nextSamplesPerChunkChangeChunkIndex = stsc.readUnsignedIntToInt() - 1;
                    }
                }

                // Expect samplesPerChunk samples in the following chunk, if it's before the end.
                if (chunkIndex < chunkCount)
                {
                    remainingSamplesInChunk = samplesPerChunk;
                }
            }
            else
            {
                // The next sample follows the current one.
                offsetBytes += sizes[i];
            }
        }

        Util.scaleLargeTimestampsInPlace(timestamps, 1000000, track.timescale);

        // Check all the expected samples have been seen.
        Assertions.checkArgument(remainingSynchronizationSamples == 0);
        Assertions.checkArgument(remainingSamplesAtTimestampDelta == 0);
        Assertions.checkArgument(remainingSamplesInChunk == 0);
        Assertions.checkArgument(remainingTimestampDeltaChanges == 0);
        Assertions.checkArgument(remainingTimestampOffsetChanges == 0);
        return new TrackSampleTable(offsets, sizes, timestamps, flags);
    }

    /**
     * Parses a mvhd atom (defined in 14496-12), returning the timescale for the movie.
     *
     * @param mvhd Contents of the mvhd atom to be parsed.
     * @return Timescale for the movie.
     */
    private static long parseMvhd(ParsableByteArray mvhd)
    {
        mvhd.setPosition(Atom.HEADER_SIZE);

        int fullAtom = mvhd.readInt();
        int version  = Atom.parseFullAtomVersion(fullAtom);

        mvhd.skipBytes(version == 0 ? 8 : 16);

        return mvhd.readUnsignedInt();
    }

    /**
     * Parses a tkhd atom (defined in 14496-12).
     *
     * @return An object containing the parsed data.
     */
    private static TkhdData parseTkhd(ParsableByteArray tkhd)
    {
        tkhd.setPosition(Atom.HEADER_SIZE);
        int fullAtom = tkhd.readInt();
        int version  = Atom.parseFullAtomVersion(fullAtom);

        tkhd.skipBytes(version == 0 ? 8 : 16);
        int trackId = tkhd.readInt();

        tkhd.skipBytes(4);
        boolean durationUnknown   = true;
        int     durationPosition  = tkhd.getPosition();
        int     durationByteCount = version == 0 ? 4 : 8;
        for (int i = 0; i < durationByteCount; i++)
        {
            if (tkhd.data[durationPosition + i] != -1)
            {
                durationUnknown = false;
                break;
            }
        }
        long duration;
        if (durationUnknown)
        {
            tkhd.skipBytes(durationByteCount);
            duration = -1;
        }
        else
        {
            duration = version == 0 ? tkhd.readUnsignedInt() : tkhd.readUnsignedLongToLong();
        }

        tkhd.skipBytes(16);
        int a00 = tkhd.readInt();
        int a01 = tkhd.readInt();
        tkhd.skipBytes(4);
        int a10 = tkhd.readInt();
        int a11 = tkhd.readInt();

        int rotationDegrees;
        int fixedOne = 65536;
        if (a00 == 0 && a01 == fixedOne && a10 == -fixedOne && a11 == 0)
        {
            rotationDegrees = 90;
        }
        else if (a00 == 0 && a01 == -fixedOne && a10 == fixedOne && a11 == 0)
        {
            rotationDegrees = 270;
        }
        else if (a00 == -fixedOne && a01 == 0 && a10 == 0 && a11 == -fixedOne)
        {
            rotationDegrees = 180;
        }
        else
        {
            // Only 0, 90, 180 and 270 are supported. Treat anything else as 0.
            rotationDegrees = 0;
        }

        return new TkhdData(trackId, duration, rotationDegrees);
    }

    /**
     * Parses an hdlr atom.
     *
     * @param hdlr The hdlr atom to parse.
     * @return The track type.
     */
    private static int parseHdlr(ParsableByteArray hdlr)
    {
        hdlr.setPosition(Atom.FULL_HEADER_SIZE + 4);
        return hdlr.readInt();
    }

    /**
     * Parses an mdhd atom (defined in 14496-12).
     *
     * @param mdhd The mdhd atom to parse.
     * @return A pair consisting of the media timescale defined as the number of time units that pass
     * in one second, and the language code.
     */
    private static Pair<Long, String> parseMdhd(ParsableByteArray mdhd)
    {
        mdhd.setPosition(Atom.HEADER_SIZE);
        int fullAtom = mdhd.readInt();
        int version  = Atom.parseFullAtomVersion(fullAtom);
        mdhd.skipBytes(version == 0 ? 8 : 16);
        long timescale = mdhd.readUnsignedInt();
        mdhd.skipBytes(version == 0 ? 4 : 8);
        int languageCode = mdhd.readUnsignedShort();
        String language = "" + (char) (((languageCode >> 10) & 0x1F) + 0x60)
                + (char) (((languageCode >> 5) & 0x1F) + 0x60)
                + (char) (((languageCode) & 0x1F) + 0x60);
        return Pair.create(timescale, language);
    }

    /**
     * Parses a stsd atom (defined in 14496-12).
     *
     * @return An object containing the parsed data.
     */
    private static StsdData parseStsd(ParsableByteArray stsd, long durationUs, int rotationDegrees,
                                      String language)
    {
        stsd.setPosition(Atom.FULL_HEADER_SIZE);
        int      numberOfEntries = stsd.readInt();
        StsdData out             = new StsdData(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++)
        {
            int childStartPosition = stsd.getPosition();
            int childAtomSize = stsd.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = stsd.readInt();
            if (childAtomType == Atom.TYPE_avc1 || childAtomType == Atom.TYPE_avc3
                    || childAtomType == Atom.TYPE_encv || childAtomType == Atom.TYPE_mp4v
                    || childAtomType == Atom.TYPE_hvc1 || childAtomType == Atom.TYPE_hev1
                    || childAtomType == Atom.TYPE_s263)
            {
                parseVideoSampleEntry(stsd,
                                      childStartPosition,
                                      childAtomSize,
                                      durationUs,
                                      rotationDegrees,
                                      out,
                                      i);
            }
            else if (childAtomType == Atom.TYPE_mp4a || childAtomType == Atom.TYPE_enca
                    || childAtomType == Atom.TYPE_ac_3)
            {
                parseAudioSampleEntry(stsd,
                                      childAtomType,
                                      childStartPosition,
                                      childAtomSize,
                                      durationUs,
                                      out,
                                      i);
            }
            else if (childAtomType == Atom.TYPE_TTML)
            {
                out.mediaFormat = MediaFormat.createTextFormat(MimeTypes.APPLICATION_TTML,
                                                               MediaFormat.NO_VALUE,
                                                               language,
                                                               durationUs);
            }
            else if (childAtomType == Atom.TYPE_tx3g)
            {
                out.mediaFormat = MediaFormat.createTextFormat(MimeTypes.APPLICATION_TX3G,
                                                               MediaFormat.NO_VALUE,
                                                               language,
                                                               durationUs);
            }
            else if (childAtomType == Atom.TYPE_stpp)
            {
                out.mediaFormat = MediaFormat.createTextFormat(MimeTypes.APPLICATION_TTML,
                                                               MediaFormat.NO_VALUE,
                                                               language,
                                                               durationUs,
                                                               0 /* subsample timing is absolute */);
            }
            stsd.setPosition(childStartPosition + childAtomSize);
        }
        return out;
    }

    private static void parseVideoSampleEntry(ParsableByteArray parent,
                                              int position,
                                              int size,
                                              long durationUs,
                                              int rotationDegrees,
                                              StsdData out,
                                              int entryIndex)
    {
        parent.setPosition(position + Atom.HEADER_SIZE);

        parent.skipBytes(24);
        int     width                         = parent.readUnsignedShort();
        int     height                        = parent.readUnsignedShort();
        boolean pixelWidthHeightRatioFromPasp = false;
        float   pixelWidthHeightRatio         = 1;
        parent.skipBytes(50);

        List<byte[]> initializationData = null;
        int          childPosition      = parent.getPosition();
        String       mimeType           = null;
        while (childPosition - position < size)
        {
            parent.setPosition(childPosition);
            int childStartPosition = parent.getPosition();
            int childAtomSize = parent.readInt();
            if (childAtomSize == 0 && parent.getPosition() - position == size)
            {
                // Handle optional terminating four zero bytes in MOV files.
                break;
            }
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_avcC)
            {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H264;
                AvcCData avcCData = parseAvcCFromParent(parent, childStartPosition);
                initializationData = avcCData.initializationData;
                out.nalUnitLengthFieldLength = avcCData.nalUnitLengthFieldLength;
                if (!pixelWidthHeightRatioFromPasp)
                {
                    pixelWidthHeightRatio = avcCData.pixelWidthAspectRatio;
                }
            }
            else if (childAtomType == Atom.TYPE_hvcC)
            {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H265;
                Pair<List<byte[]>, Integer> hvcCData = parseHvcCFromParent(parent,
                                                                           childStartPosition);
                initializationData = hvcCData.first;
                out.nalUnitLengthFieldLength = hvcCData.second;
            }
            else if (childAtomType == Atom.TYPE_d263)
            {
                Assertions.checkState(mimeType == null);
                mimeType = MimeTypes.VIDEO_H263;
            }
            else if (childAtomType == Atom.TYPE_esds)
            {
                Assertions.checkState(mimeType == null);
                Pair<String, byte[]> mimeTypeAndInitializationData =
                        parseEsdsFromParent(parent, childStartPosition);
                mimeType = mimeTypeAndInitializationData.first;
                initializationData = Collections.singletonList(mimeTypeAndInitializationData.second);
            }
            else if (childAtomType == Atom.TYPE_sinf)
            {
                out.trackEncryptionBoxes[entryIndex] =
                        parseSinfFromParent(parent, childStartPosition, childAtomSize);
            }
            else if (childAtomType == Atom.TYPE_pasp)
            {
                pixelWidthHeightRatio = parsePaspFromParent(parent, childStartPosition);
                pixelWidthHeightRatioFromPasp = true;
            }
            childPosition += childAtomSize;
        }

        // If the media type was not recognized, ignore the track.
        if (mimeType == null)
        {
            return;
        }

        out.mediaFormat = MediaFormat.createVideoFormat(mimeType,
                                                        MediaFormat.NO_VALUE,
                                                        MediaFormat.NO_VALUE,
                                                        durationUs,
                                                        width,
                                                        height,
                                                        rotationDegrees,
                                                        pixelWidthHeightRatio,
                                                        initializationData);
    }

    private static AvcCData parseAvcCFromParent(ParsableByteArray parent, int position)
    {
        parent.setPosition(position + Atom.HEADER_SIZE + 4);
        // Start of the AVCDecoderConfigurationRecord (defined in 14496-15)
        int nalUnitLengthFieldLength = (parent.readUnsignedByte() & 0x3) + 1;
        if (nalUnitLengthFieldLength == 3)
        {
            throw new IllegalStateException();
        }
        List<byte[]> initializationData       = new ArrayList<>();
        float        pixelWidthAspectRatio    = 1;
        int          numSequenceParameterSets = parent.readUnsignedByte() & 0x1F;
        for (int j = 0; j < numSequenceParameterSets; j++)
        {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }
        int numPictureParameterSets = parent.readUnsignedByte();
        for (int j = 0; j < numPictureParameterSets; j++)
        {
            initializationData.add(NalUnitUtil.parseChildNalUnit(parent));
        }

        if (numSequenceParameterSets > 0)
        {
            // Parse the first sequence parameter set to obtain pixelWidthAspectRatio.
            ParsableBitArray spsDataBitArray = new ParsableBitArray(initializationData.get(0));
            // Skip the NAL header consisting of the nalUnitLengthField and the type (1 byte).
            spsDataBitArray.setPosition(8 * (nalUnitLengthFieldLength + 1));
            pixelWidthAspectRatio = CodecSpecificDataUtil.parseSpsNalUnit(spsDataBitArray)
                    .pixelWidthAspectRatio;
        }

        return new AvcCData(initializationData, nalUnitLengthFieldLength, pixelWidthAspectRatio);
    }

    private static Pair<List<byte[]>, Integer> parseHvcCFromParent(ParsableByteArray parent,
                                                                   int position)
    {
        // Skip to the NAL unit length size field.
        parent.setPosition(position + Atom.HEADER_SIZE + 21);
        int lengthSizeMinusOne = parent.readUnsignedByte() & 0x03;

        // Calculate the combined size of all VPS/SPS/PPS bitstreams.
        int numberOfArrays   = parent.readUnsignedByte();
        int csdLength        = 0;
        int csdStartPosition = parent.getPosition();
        for (int i = 0; i < numberOfArrays; i++)
        {
            parent.skipBytes(1); // completeness (1), nal_unit_type (7)
            int numberOfNalUnits = parent.readUnsignedShort();
            for (int j = 0; j < numberOfNalUnits; j++)
            {
                int nalUnitLength = parent.readUnsignedShort();
                csdLength += 4 + nalUnitLength; // Start code and NAL unit.
                parent.skipBytes(nalUnitLength);
            }
        }

        // Concatenate the codec-specific data into a single buffer.
        parent.setPosition(csdStartPosition);
        byte[] buffer         = new byte[csdLength];
        int    bufferPosition = 0;
        for (int i = 0; i < numberOfArrays; i++)
        {
            parent.skipBytes(1); // completeness (1), nal_unit_type (7)
            int numberOfNalUnits = parent.readUnsignedShort();
            for (int j = 0; j < numberOfNalUnits; j++)
            {
                int nalUnitLength = parent.readUnsignedShort();
                System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, buffer, bufferPosition,
                                 NalUnitUtil.NAL_START_CODE.length);
                bufferPosition += NalUnitUtil.NAL_START_CODE.length;
                System.arraycopy(parent.data,
                                 parent.getPosition(),
                                 buffer,
                                 bufferPosition,
                                 nalUnitLength);
                bufferPosition += nalUnitLength;
                parent.skipBytes(nalUnitLength);
            }
        }

        List<byte[]> initializationData = csdLength == 0 ? null : Collections.singletonList(buffer);
        return Pair.create(initializationData, lengthSizeMinusOne + 1);
    }

    private static TrackEncryptionBox parseSinfFromParent(ParsableByteArray parent, int position,
                                                          int size)
    {
        int childPosition = position + Atom.HEADER_SIZE;

        TrackEncryptionBox trackEncryptionBox = null;
        while (childPosition - position < size)
        {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_frma)
            {
                parent.readInt(); // dataFormat.
            }
            else if (childAtomType == Atom.TYPE_schm)
            {
                parent.skipBytes(4);
                parent.readInt(); // schemeType. Expect cenc
                parent.readInt(); // schemeVersion. Expect 0x00010000
            }
            else if (childAtomType == Atom.TYPE_schi)
            {
                trackEncryptionBox = parseSchiFromParent(parent, childPosition, childAtomSize);
            }
            childPosition += childAtomSize;
        }

        return trackEncryptionBox;
    }

    private static float parsePaspFromParent(ParsableByteArray parent, int position)
    {
        parent.setPosition(position + Atom.HEADER_SIZE);
        int hSpacing = parent.readUnsignedIntToInt();
        int vSpacing = parent.readUnsignedIntToInt();
        return (float) hSpacing / vSpacing;
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parent, int position,
                                                          int size)
    {
        int childPosition = position + Atom.HEADER_SIZE;
        while (childPosition - position < size)
        {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_tenc)
            {
                parent.skipBytes(4);
                int firstInt = parent.readInt();
                boolean defaultIsEncrypted = (firstInt >> 8) == 1;
                int defaultInitVectorSize = firstInt & 0xFF;
                byte[] defaultKeyId = new byte[16];
                parent.readBytes(defaultKeyId, 0, defaultKeyId.length);
                return new TrackEncryptionBox(defaultIsEncrypted,
                                              defaultInitVectorSize,
                                              defaultKeyId);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static void parseAudioSampleEntry(ParsableByteArray parent,
                                              int atomType,
                                              int position,
                                              int size,
                                              long durationUs,
                                              StsdData out,
                                              int entryIndex)
    {
        parent.setPosition(position + Atom.HEADER_SIZE);
        parent.skipBytes(16);
        int channelCount = parent.readUnsignedShort();
        int sampleSize   = parent.readUnsignedShort();
        parent.skipBytes(4);
        int sampleRate = parent.readUnsignedFixedPoint1616();

        // If the atom type determines a MIME type, set it immediately.
        String mimeType = null;
        if (atomType == Atom.TYPE_ac_3)
        {
            mimeType = MimeTypes.AUDIO_AC3;
        }
        else if (atomType == Atom.TYPE_ec_3)
        {
            mimeType = MimeTypes.AUDIO_EC3;
        }

        byte[] initializationData = null;
        int    childPosition      = parent.getPosition();
        while (childPosition - position < size)
        {
            parent.setPosition(childPosition);
            int childStartPosition = parent.getPosition();
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (atomType == Atom.TYPE_mp4a || atomType == Atom.TYPE_enca)
            {
                if (childAtomType == Atom.TYPE_esds)
                {
                    Pair<String, byte[]> mimeTypeAndInitializationData =
                            parseEsdsFromParent(parent, childStartPosition);
                    mimeType = mimeTypeAndInitializationData.first;
                    initializationData = mimeTypeAndInitializationData.second;
                    if (MimeTypes.AUDIO_AAC.equals(mimeType))
                    {
                        // TODO: Do we really need to do this? See [Internal: b/10903778]
                        // Update sampleRate and channelCount from the AudioSpecificConfig initialization data.
                        Pair<Integer, Integer> audioSpecificConfig =
                                CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData);
                        sampleRate = audioSpecificConfig.first;
                        channelCount = audioSpecificConfig.second;
                    }
                }
                else if (childAtomType == Atom.TYPE_sinf)
                {
                    out.trackEncryptionBoxes[entryIndex] = parseSinfFromParent(parent,
                                                                               childStartPosition,
                                                                               childAtomSize);
                }
            }
            else if (atomType == Atom.TYPE_ac_3 && childAtomType == Atom.TYPE_dac3)
            {
                // TODO: Choose the right AC-3 track based on the contents of dac3/dec3.
                // TODO: Add support for encryption (by setting out.trackEncryptionBoxes).
                parent.setPosition(Atom.HEADER_SIZE + childStartPosition);
                out.mediaFormat = Ac3Util.parseAnnexFAc3Format(parent);
                return;
            }
            else if (atomType == Atom.TYPE_ec_3 && childAtomType == Atom.TYPE_dec3)
            {
                parent.setPosition(Atom.HEADER_SIZE + childStartPosition);
                out.mediaFormat = Ac3Util.parseAnnexFEAc3Format(parent);
                return;
            }
            childPosition += childAtomSize;
        }

        // If the media type was not recognized, ignore the track.
        if (mimeType == null)
        {
            return;
        }

        out.mediaFormat = MediaFormat.createAudioFormat(mimeType, MediaFormat.NO_VALUE, sampleSize,
                                                        durationUs, channelCount, sampleRate,
                                                        initializationData == null ? null : Collections
                                                                .singletonList(initializationData));
    }

    /**
     * Returns codec-specific initialization data contained in an esds box.
     */
    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parent, int position)
    {
        parent.setPosition(position + Atom.HEADER_SIZE + 4);
        // Start of the ES_Descriptor (defined in 14496-1)
        parent.skipBytes(1); // ES_Descriptor tag
        int varIntByte = parent.readUnsignedByte();
        while (varIntByte > 127)
        {
            varIntByte = parent.readUnsignedByte();
        }
        parent.skipBytes(2); // ES_ID

        int flags = parent.readUnsignedByte();
        if ((flags & 0x80 /* streamDependenceFlag */) != 0)
        {
            parent.skipBytes(2);
        }
        if ((flags & 0x40 /* URL_Flag */) != 0)
        {
            parent.skipBytes(parent.readUnsignedShort());
        }
        if ((flags & 0x20 /* OCRstreamFlag */) != 0)
        {
            parent.skipBytes(2);
        }

        // Start of the DecoderConfigDescriptor (defined in 14496-1)
        parent.skipBytes(1); // DecoderConfigDescriptor tag
        varIntByte = parent.readUnsignedByte();
        while (varIntByte > 127)
        {
            varIntByte = parent.readUnsignedByte();
        }

        // Set the MIME type based on the object type indication (14496-1 table 5).
        int    objectTypeIndication = parent.readUnsignedByte();
        String mimeType;
        switch (objectTypeIndication)
        {
            case 0x6B:
                mimeType = MimeTypes.AUDIO_MPEG;
                // Don't extract codec-specific data for MPEG audio tracks, as it is not needed.
                return Pair.create(mimeType, null);
            case 0x20:
                mimeType = MimeTypes.VIDEO_MP4V;
                break;
            case 0x21:
                mimeType = MimeTypes.VIDEO_H264;
                break;
            case 0x23:
                mimeType = MimeTypes.VIDEO_H265;
                break;
            case 0x40:
                mimeType = MimeTypes.AUDIO_AAC;
                break;
            case 0xA5:
                mimeType = MimeTypes.AUDIO_AC3;
                break;
            case 0xA6:
                mimeType = MimeTypes.AUDIO_EC3;
                break;
            default:
                mimeType = null;
                break;
        }

        parent.skipBytes(12);

        // Start of the AudioSpecificConfig.
        parent.skipBytes(1); // AudioSpecificConfig tag
        varIntByte = parent.readUnsignedByte();
        int varInt = varIntByte & 0x7F;
        while (varIntByte > 127)
        {
            varIntByte = parent.readUnsignedByte();
            varInt = varInt << 8;
            varInt |= varIntByte & 0x7F;
        }
        byte[] initializationData = new byte[varInt];
        parent.readBytes(initializationData, 0, varInt);
        return Pair.create(mimeType, initializationData);
    }

    private AtomParsers()
    {
        // Prevent instantiation.
    }

    /**
     * Holds data parsed from a tkhd atom.
     */
    private static final class TkhdData
    {

        private final int  id;
        private final long duration;
        private final int  rotationDegrees;

        public TkhdData(int id, long duration, int rotationDegrees)
        {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }

    }

    /**
     * Holds data parsed from an stsd atom and its children.
     */
    private static final class StsdData
    {

        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public MediaFormat mediaFormat;
        public int         nalUnitLengthFieldLength;

        public StsdData(int numberOfEntries)
        {
            trackEncryptionBoxes = new TrackEncryptionBox[numberOfEntries];
            nalUnitLengthFieldLength = -1;
        }

    }

    /**
     * Holds data parsed from an AvcC atom.
     */
    private static final class AvcCData
    {

        public final List<byte[]> initializationData;
        public final int          nalUnitLengthFieldLength;
        public final float        pixelWidthAspectRatio;

        public AvcCData(List<byte[]> initializationData, int nalUnitLengthFieldLength,
                        float pixelWidthAspectRatio)
        {
            this.initializationData = initializationData;
            this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }

    }

}
