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
package com.google.android.exoplayer.text.subrip;

import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.text.Subtitle;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;

import java.util.Collections;
import java.util.List;

/**
 * A representation of a SubRip subtitle.
 */
/* package */ final class SubripSubtitle implements Subtitle
{

    private final Cue[]  cues;
    private final long[] cueTimesUs;

    /**
     * @param cues       The cues in the subtitle.
     * @param cueTimesUs Interleaved cue start and end times, in microseconds.
     */
    public SubripSubtitle(Cue[] cues, long[] cueTimesUs)
    {
        this.cues = cues;
        this.cueTimesUs = cueTimesUs;
    }

    @Override
    public int getNextEventTimeIndex(long timeUs)
    {
        int index = Util.binarySearchCeil(cueTimesUs, timeUs, false, false);
        return index < cueTimesUs.length ? index : -1;
    }

    @Override
    public int getEventTimeCount()
    {
        return cueTimesUs.length;
    }

    @Override
    public long getEventTime(int index)
    {
        Assertions.checkArgument(index >= 0);
        Assertions.checkArgument(index < cueTimesUs.length);
        return cueTimesUs[index];
    }

    @Override
    public long getLastEventTime()
    {
        if (getEventTimeCount() == 0)
        {
            return -1;
        }
        return cueTimesUs[cueTimesUs.length - 1];
    }

    @Override
    public List<Cue> getCues(long timeUs)
    {
        int index = Util.binarySearchFloor(cueTimesUs, timeUs, true, false);
        if (index == -1 || index % 2 == 1)
        {
            // timeUs is earlier than the start of the first cue, or corresponds to a gap between cues.
            return Collections.<Cue>emptyList();
        }
        else
        {
            return Collections.singletonList(cues[index / 2]);
        }
    }

}
