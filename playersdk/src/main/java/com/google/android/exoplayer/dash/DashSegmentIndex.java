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
package com.google.android.exoplayer.dash;

import com.google.android.exoplayer.dash.mpd.RangedUri;

/**
 * Indexes the segments within a media stream.
 * <p/>
 * TODO: Generalize to cover all chunk streaming modes (e.g. SmoothStreaming) if possible.
 */
public interface DashSegmentIndex
{

    public static final int INDEX_UNBOUNDED = -1;

    /**
     * Returns the segment number of the segment containing a given media time.
     * <p/>
     * If the given media time is outside the range of the index, then the returned segment number is
     * clamped to {@link #getFirstSegmentNum()} (if the given media time is earlier the start of the
     * first segment) or {@link #getLastSegmentNum()} (if the given media time is later then the end
     * of the last segment).
     *
     * @param timeUs The time in microseconds.
     * @return The segment number of the corresponding segment.
     */
    int getSegmentNum(long timeUs);

    /**
     * Returns the start time of a segment.
     *
     * @param segmentNum The segment number.
     * @return The corresponding start time in microseconds.
     */
    long getTimeUs(int segmentNum);

    /**
     * Returns the duration of a segment.
     *
     * @param segmentNum The segment number.
     * @return The duration of the segment, in microseconds.
     */
    long getDurationUs(int segmentNum);

    /**
     * Returns a {@link RangedUri} defining the location of a segment.
     *
     * @param segmentNum The segment number.
     * @return The {@link RangedUri} defining the location of the data.
     */
    RangedUri getSegmentUrl(int segmentNum);

    /**
     * Returns the segment number of the first segment.
     *
     * @return The segment number of the first segment.
     */
    int getFirstSegmentNum();

    /**
     * Returns the segment number of the last segment, or {@link #INDEX_UNBOUNDED}.
     * <p/>
     * An unbounded index occurs if a live stream manifest uses SegmentTemplate elements without a
     * SegmentTimeline element. In this case the manifest can be used to derive information about
     * segments arbitrarily far into the future. This means that the manifest does not need to be
     * refreshed as frequently (if at all) during playback, however it is necessary for a player to
     * manually calculate the window of currently available segments.
     *
     * @return The segment number of the last segment, or {@link #INDEX_UNBOUNDED}.
     */
    int getLastSegmentNum();

    /**
     * Returns true if segments are defined explicitly by the index.
     * <p/>
     * If true is returned, each segment is defined explicitly by the index data, and all of the
     * listed segments are guaranteed to be available at the time when the index was obtained.
     * <p/>
     * If false is returned then segment information was derived from properties such as a fixed
     * segment duration. If the presentation is dynamic, it's possible that only a subset of the
     * segments are available.
     *
     * @return True if segments are defined explicitly by the index. False otherwise.
     */
    boolean isExplicit();

}
