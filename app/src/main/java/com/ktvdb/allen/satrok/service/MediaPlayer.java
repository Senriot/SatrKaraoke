package com.ktvdb.allen.satrok.service;

import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.view.Surface;
import android.widget.MediaController;

import com.apkfuns.logutils.LogUtils;
import com.google.android.exoplayer.CodecCounters;
import com.google.android.exoplayer.DummyTrackRenderer;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.event.PlayWhenReadyEvent;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.NewokMedia;
import com.ktvdb.allen.satrok.model.Song;

import org.apache.commons.lang3.StringUtils;
import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Allen on 15/8/29.
 */

public class MediaPlayer implements MediaController.MediaPlayerControl,
                                    MediaCodecVideoTrackRenderer.EventListener,
                                    MediaCodecAudioTrackRenderer.EventListener,
                                    ExoPlayer.Listener
{
    ExoPlayer mPlayer;

    android.media.MediaPlayer mMediaPlayer;

    PlayRecordService mRecordService;

    public static final int RENDERER_COUNT = 2;
    public static final int TYPE_VIDEO     = 0;
    public static final int TYPE_AUDIO     = 1;

    private static final int BUFFER_SEGMENT_SIZE  = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 160;

    private static final int RENDERER_BUILDING_STATE_IDLE     = 1;
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT    = 3;

    private final Handler               mainHandler;
    private       TrackRenderer         videoRenderer;
    private       TrackRenderer         vRenderer;
    private       CodecCounters         codecCounters;
    private       DefaultBandwidthMeter bandwidthMeter;
    private       Surface               surface;

    private NewokMedia media;
    private int        rendererBuildingState;


    public NewokMedia getMedia()
    {
        return media;
    }


    private LinkedList<NewokMedia> mPlayList = new LinkedList<>();

    private List<NewokMedia> mPlayedList = new ArrayList<>();

    public void setMedia(NewokMedia media)
    {
        this.media = media;
    }

    public Handler getMainHandler()
    {
        return mainHandler;
    }

    public LinkedList<NewokMedia> getPlayList()
    {
        return mPlayList;
    }

    public List<NewokMedia> getPlayedList()
    {
        return mPlayedList;
    }

    public MediaPlayer()
    {
        this.mPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT);
        this.mPlayer.addListener(this);
        mainHandler = new Handler();
        mMediaPlayer = new android.media.MediaPlayer();
        mRecordService = StarokApplication.getAppContext().getComponent().playRecordService();
    }

    @Override
    public void start()
    {
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void pause()
    {
        mPlayer.setPlayWhenReady(false);
    }

    @Override
    public int getDuration()
    {
        return mPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) mPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition()
    {
        return mPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : (int) mPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos)
    {
        long seekPosition = mPlayer.getDuration() == ExoPlayer.UNKNOWN_TIME ? 0
                : Math.min(Math.max(0, pos), getDuration());
        mPlayer.seekTo(seekPosition);
    }

    @Override
    public boolean isPlaying()
    {
        return mPlayer.getPlayWhenReady();
    }

    @Override
    public int getBufferPercentage()
    {
        return 0;
    }

    @Override
    public boolean canPause()
    {
        return true;
    }

    @Override
    public boolean canSeekBackward()
    {
        return true;
    }

    @Override
    public boolean canSeekForward()
    {
        return true;
    }

    @Override
    public int getAudioSessionId()
    {
        return 0;
    }

    @Override
    public void onAudioTrackInitializationError(AudioTrack.InitializationException e)
    {

    }

    @Override
    public void onAudioTrackWriteError(AudioTrack.WriteException e)
    {

    }

    @Override
    public void onDroppedFrames(int count, long elapsed)
    {

    }

    @Override
    public void onVideoSizeChanged(int width,
                                   int height,
                                   int unappliedRotationDegrees,
                                   float pixelWidthHeightRatio)
    {

    }

    @Override
    public void onDrawnToSurface(Surface surface)
    {
    }

    @Override
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e)
    {

    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e)
    {

    }

    @Override
    public void onDecoderInitialized(String decoderName,
                                     long elapsedRealtimeMs,
                                     long initializationDurationMs)
    {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {
        LogUtils.e("playWhenReady:" + playWhenReady + " playbackState:" + playbackState);
        EventBus.getDefault().post(new PlayWhenReadyEvent(playWhenReady, playbackState));
        if (playWhenReady && playbackState == ExoPlayer.STATE_ENDED)
        {
            onCut();
        }
    }

    @Override
    public void onPlayWhenReadyCommitted()
    {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error)
    {

    }

    public void onRenderers(TrackRenderer[] renderers, DefaultBandwidthMeter bandwidthMeter)
    {
        for (int i = 0; i < RENDERER_COUNT; i++)
        {
            if (renderers[i] == null)
            {
                // Convert a null renderer to a dummy renderer.
                renderers[i] = new DummyTrackRenderer();
            }
        }
        // Complete preparation.
        this.videoRenderer = renderers[TYPE_VIDEO];
        this.vRenderer = renderers[TYPE_AUDIO];
        this.codecCounters = videoRenderer instanceof MediaCodecTrackRenderer
                ? ((MediaCodecTrackRenderer) videoRenderer).codecCounters
                : renderers[TYPE_AUDIO] instanceof MediaCodecTrackRenderer
                ? ((MediaCodecTrackRenderer) renderers[TYPE_AUDIO]).codecCounters : null;
        this.bandwidthMeter = bandwidthMeter;
        pushSurface(false);
        mPlayer.prepare(renderers);
    }

    private void pushSurface(boolean blockForSurfacePush)
    {
        if (videoRenderer == null)
        {
            return;
        }


        if (blockForSurfacePush)
        {
            mPlayer.blockingSendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        }
        else
        {
            mPlayer.sendMessage(
                    videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);
        }
    }

    public void prepare()
    {
        if (rendererBuildingState == RENDERER_BUILDING_STATE_BUILT)
        {
            mPlayer.stop();
        }
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
        buildRenderers(media.getPlayUrl());
    }


    public void buildRenderers(String url)
    {
        Allocator             allocator      = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(getMainHandler(), null);
        DataSource dataSource = new DefaultUriDataSource(StarokApplication.getAppContext(),
                                                         Util.getUserAgent(
                                                                 StarokApplication.getAppContext(),
                                                                 "Starok"));

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(Uri.parse(url),
                                                                       dataSource,
                                                                       allocator,
                                                                       BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);
        MediaCodecVideoTrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource,
                                                                                      null,
                                                                                      true,
                                                                                      MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT,
                                                                                      5000,
                                                                                      null,
                                                                                      getMainHandler(),
                                                                                      this,
                                                                                      50);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource,
                                                                                      null,
                                                                                      true,
                                                                                      getMainHandler(),
                                                                                      this,
                                                                                      AudioCapabilities
                                                                                              .getCapabilities(
                                                                                                      StarokApplication
                                                                                                              .getAppContext()));

        // Invoke the callback.
        TrackRenderer[] renderers = new TrackRenderer[MediaPlayer.RENDERER_COUNT];
        renderers[MediaPlayer.TYPE_VIDEO] = videoRenderer;
        renderers[MediaPlayer.TYPE_AUDIO] = audioRenderer;
        onRenderers(renderers, bandwidthMeter);
    }


    public void setSurface(Surface surface)
    {
        this.surface = surface;
        pushSurface(false);
    }

    public void rePlay()
    {
        mPlayer.seekTo(0);
    }

    /**
     * 获取音轨
     *
     * @return
     */
    public int getAudioTrackCount()
    {
        return mPlayer.getTrackCount(TYPE_AUDIO);
    }

    /**
     * 切换音轨
     *
     * @param index
     */
    public void setSelectedTrack(int index)
    {
        mPlayer.setSelectedTrack(TYPE_AUDIO, index);
    }

    public int getSelectedTrack()
    {
        return mPlayer.getSelectedTrack(TYPE_AUDIO);
    }

    public void setChannel(int channel)
    {
        mPlayer.sendMessage(this.vRenderer, MediaCodecAudioTrackRenderer.MSG_SET_CHANNEL, channel);
    }

    /**
     * 切歌
     */
    public void onCut()
    {
        if (!mPlayList.isEmpty())
        {
            if (media != null)
            {
                mPlayedList.add(media);
                mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_END);
            }
            mPlayer.stop();
            media = mPlayList.removeFirst();
            startPlay();
            mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_START);
        }
        else
        {
            media = null;
        }
        EventBus.getDefault().post(new PlayQueueChengedEvent());
    }


    public void startPlay()
    {
        prepare();
        mPlayer.seekTo(0);
        start();
//        setSelectedTrack(0);
//        int track = getAudioTrackCount();
//        if (track == 1)
//        {
//            setSelectedTrack(0);
//        }
    }


    /**
     * 添加节目到播放列表
     *
     * @param media 节目
     */
    public void addMedia(NewokMedia media, boolean isFirst)
    {
        if (StringUtils.isBlank(media.getPlayUrl())) return;
        if (this.media == null)
        {
            this.media = media;
            startPlay();
            mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_START);
        }
        else if (!mPlayList.contains(media))
        {
            if (isFirst)
            {
                mPlayList.addFirst(media);
                mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_TOP);

            }
            else
            {
                mPlayList.addLast(media);
                mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_SELECTED);
            }
        }
        else if (isFirst)
        {
            mPlayList.addFirst(mPlayList.remove(mPlayList.indexOf(media)));
            mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_TOP);
        }
        EventBus.getDefault().post(new PlayQueueChengedEvent());
    }

    /**
     * 置顶歌曲
     *
     * @param media 歌曲
     */
    public void AddFistMedia(NewokMedia media)
    {
        addMedia(media, true);
    }

    /**
     * 添加歌曲
     *
     * @param media
     */
    public void addLastMedia(NewokMedia media)
    {
        addMedia(media, false);
    }

    /**
     * 删除歌曲
     *
     * @param media
     */
    public void delMedia(NewokMedia media)
    {
        mPlayList.remove(media);
        EventBus.getDefault().post(new PlayQueueChengedEvent());
        mRecordService.recordMedia(media, PlayRecordService.ACTION_TYPE_DELETE);
    }

    public boolean isSelected(NewokMedia media)
    {
        return mPlayList.contains(media);
    }

    public int selectIndex(NewokMedia media)
    {
        return mPlayList.indexOf(media);
    }


    public void playLocalMp3(AssetFileDescriptor fileDescriptor)
    {
        if (mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        try
        {
            mMediaPlayer.setOnCompletionListener(mp -> {
                mMediaPlayer.reset();
                try
                {
                    fileDescriptor.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
            mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                                       fileDescriptor.getStartOffset(),
                                       fileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void onPlayMovie(Movie mMovie)
    {
        if (StringUtils.isBlank(mMovie.getPlayUrl())) return;
        media = mMovie;
        mPlayer.stop();
        startPlay();
    }
}
