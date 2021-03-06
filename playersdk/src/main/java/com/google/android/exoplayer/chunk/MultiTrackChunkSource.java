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
package com.google.android.exoplayer.chunk;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer.ExoPlayerComponent;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.util.Assertions;

import java.io.IOException;
import java.util.List;

/**
 * A {@link ChunkSource} providing the ability to switch between multiple other {@link ChunkSource}
 * instances.
 */
// TODO: Expose multiple tracks directly in DashChunkSource and SmoothStreamingChunkSource, and
// delete this class.
public final class MultiTrackChunkSource implements ChunkSource,
                                                    ExoPlayerComponent
{

    /**
     * A message to indicate a source selection. Source selection can only be performed when the
     * source is disabled.
     */
    public static final int MSG_SELECT_TRACK = 1;

    private final ChunkSource[] allSources;

    private ChunkSource selectedSource;
    private boolean     enabled;

    public MultiTrackChunkSource(ChunkSource... sources)
    {
        this.allSources = sources;
        this.selectedSource = sources[0];
    }

    public MultiTrackChunkSource(List<ChunkSource> sources)
    {
        this(toChunkSourceArray(sources));
    }

    /**
     * Gets the number of tracks that this source can switch between. May be called safely from any
     * thread.
     *
     * @return The number of tracks.
     */
    public int getMultiTrackCount()
    {
        return allSources.length;
    }

    @Override
    public boolean prepare()
    {
        boolean prepared = true;
        for (int i = 0; i < allSources.length; i++)
        {
            prepared &= allSources[i].prepare();
        }
        return prepared;
    }

    @Override
    public int getTrackCount()
    {
        return selectedSource.getTrackCount();
    }

    @Override
    public MediaFormat getFormat(int track)
    {
        return selectedSource.getFormat(track);
    }

    @Override
    public void enable(int track)
    {
        selectedSource.enable(track);
        enabled = true;
    }

    @Override
    public void disable(List<? extends MediaChunk> queue)
    {
        selectedSource.disable(queue);
        enabled = false;
    }

    @Override
    public void continueBuffering(long playbackPositionUs)
    {
        selectedSource.continueBuffering(playbackPositionUs);
    }

    @Override
    public void getChunkOperation(List<? extends MediaChunk> queue, long seekPositionUs,
                                  long playbackPositionUs, ChunkOperationHolder out)
    {
        selectedSource.getChunkOperation(queue, seekPositionUs, playbackPositionUs, out);
    }

    @Override
    public void maybeThrowError() throws IOException
    {
        selectedSource.maybeThrowError();
    }

    @Override
    public MediaFormat getWithMaxVideoDimensions(MediaFormat format)
    {
        return selectedSource.getWithMaxVideoDimensions(format);
    }

    @Override
    public void handleMessage(int what, Object msg) throws ExoPlaybackException
    {
        Assertions.checkState(!enabled);
        if (what == MSG_SELECT_TRACK)
        {
            selectedSource = allSources[(Integer) msg];
        }
    }

    @Override
    public void onChunkLoadCompleted(Chunk chunk)
    {
        selectedSource.onChunkLoadCompleted(chunk);
    }

    @Override
    public void onChunkLoadError(Chunk chunk, Exception e)
    {
        selectedSource.onChunkLoadError(chunk, e);
    }

    private static ChunkSource[] toChunkSourceArray(List<ChunkSource> sources)
    {
        ChunkSource[] chunkSourceArray = new ChunkSource[sources.size()];
        sources.toArray(chunkSourceArray);
        return chunkSourceArray;
    }

}
