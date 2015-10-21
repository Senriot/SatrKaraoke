package com.ktvdb.allen.satrok.event;

/**
 * Created by Allen on 15/8/30.
 */
public class PlayWhenReadyEvent
{
    public final boolean playWhenReady;
    public final int     playbackState;

    public PlayWhenReadyEvent(boolean playWhenReady, int playbackState)
    {
        this.playWhenReady = playWhenReady;
        this.playbackState = playbackState;
    }
}
