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
package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.extractor.Extractor;
import com.google.android.exoplayer.extractor.ExtractorInput;
import com.google.android.exoplayer.extractor.ExtractorOutput;
import com.google.android.exoplayer.extractor.PositionHolder;
import com.google.android.exoplayer.extractor.SeekMap;
import com.google.android.exoplayer.util.ParsableByteArray;
import com.google.android.exoplayer.util.Util;

import java.io.IOException;

/**
 * Facilitates the extraction of AAC samples from elementary audio files formatted as AAC with ADTS
 * headers.
 */
public final class AdtsExtractor implements Extractor
{

    private static final int MAX_PACKET_SIZE = 200;

    private final long              firstSampleTimestampUs;
    private final ParsableByteArray packetBuffer;

    // Accessed only by the loading thread.
    private AdtsReader adtsReader;
    private boolean    firstPacket;

    public AdtsExtractor()
    {
        this(0);
    }

    public AdtsExtractor(long firstSampleTimestampUs)
    {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        packetBuffer = new ParsableByteArray(MAX_PACKET_SIZE);
        firstPacket = true;
    }

    @Override
    public boolean sniff(ExtractorInput input) throws IOException, InterruptedException
    {
        ParsableByteArray scratch = new ParsableByteArray(10);
        input.peekFully(scratch.data, 0, 10);
        int value = scratch.readUnsignedInt24();
        if (value != Util.getIntegerCodeForString("ID3"))
        {
            value = value >> 8;
        }
        else
        {
            int length = (scratch.data[6] & 0x7F) << 21 | ((scratch.data[7] & 0x7F) << 14)
                    | ((scratch.data[8] & 0x7F) << 7) | (scratch.data[9] & 0x7F);
            input.advancePeekPosition(length);
            input.peekFully(scratch.data, 0, 2);
            scratch.setPosition(0);
            value = scratch.readUnsignedShort();
        }
        return (value & 0xFFF6) == 0xFFF0;
    }

    @Override
    public void init(ExtractorOutput output)
    {
        adtsReader = new AdtsReader(output.track(0));
        output.endTracks();
        output.seekMap(SeekMap.UNSEEKABLE);
    }

    @Override
    public void seek()
    {
        firstPacket = true;
        adtsReader.seek();
    }

    @Override
    public int read(ExtractorInput input, PositionHolder seekPosition)
            throws IOException, InterruptedException
    {
        int bytesRead = input.read(packetBuffer.data, 0, MAX_PACKET_SIZE);
        if (bytesRead == -1)
        {
            return RESULT_END_OF_INPUT;
        }

        // Feed whatever data we have to the reader, regardless of whether the read finished or not.
        packetBuffer.setPosition(0);
        packetBuffer.setLimit(bytesRead);

        // TODO: Make it possible for adtsReader to consume the dataSource directly, so that it becomes
        // unnecessary to copy the data through packetBuffer.
        adtsReader.consume(packetBuffer, firstSampleTimestampUs, firstPacket);
        firstPacket = false;
        return RESULT_CONTINUE;
    }

}
