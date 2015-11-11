package com.ktvdb.allen.satrok.gui;

import android.app.Presentation;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.databinding.library.baseAdapters.BR;
import com.apkfuns.logutils.LogUtils;
import com.badoo.mobile.util.WeakHandler;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer.ExoPlayer;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.HdmiDisplayBinding;
import com.ktvdb.allen.satrok.event.OnServiceEvent;
import com.ktvdb.allen.satrok.event.PlayWhenReadyEvent;
import com.ktvdb.allen.satrok.event.PlayerReadyedEvent;
import com.ktvdb.allen.satrok.event.ShowNextEvent;
import com.ktvdb.allen.satrok.event.VolumeChangedEvent;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.presentation.HdmiDisplayPresentation;
import com.ktvdb.allen.satrok.presentation.view.HdmiDisplayView;
import com.ktvdb.allen.satrok.service.MediaPlayer;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.MessageFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Allen on 15/8/29.
 */

public class HdmiDisplay extends Presentation implements SurfaceHolder.Callback,
                                                         HdmiDisplayView
{

    private final static int MSG_SHOW_IMAGE_AD = 1;
    private final static int MSG_HIDE_IMAGE_AD = 2;
    private final static int MSG_SHOW_SERVICE  = 3;
    private final static int MSG_SHOW_STATUS   = 4;
    private final static int MSG_HIDE_STATUS   = 5;

    HdmiDisplayBinding      mBinding;
    HdmiDisplayPresentation mPresentation;

    private long time = 0;

    private Timer timer;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MSG_SHOW_IMAGE_AD:
//                    YoYo.with(Techniques.SlideInRight).playOn(mBinding.imageAdPanl);
                    mBinding.imageAdPanl.setVisibility(View.VISIBLE);
                    break;
                case MSG_HIDE_IMAGE_AD:
//                    YoYo.with(Techniques.SlideOutRight).playOn(mBinding.imageAdPanl);
                    mBinding.imageAdPanl.setVisibility(View.GONE);
                    break;
                case MSG_SHOW_SERVICE:
                    int v = mBinding.serviceFlag.getVisibility();
                    mBinding.serviceFlag.setVisibility(v != View.VISIBLE ? View.VISIBLE : View.GONE);
                    break;
                case MSG_SHOW_STATUS:
                    mBinding.statusFlag.setVisibility(View.VISIBLE);
                    break;
                case MSG_HIDE_STATUS:
                    mBinding.statusFlag.setVisibility(View.GONE);
                    break;
            }
        }
    };
    MediaPlayer mPlayer;

    public HdmiDisplay(Context outerContext, Display display)
    {
        super(outerContext, display);
        mPresentation = new HdmiDisplayPresentation(this);
        EventBus.getDefault().register(this);
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }


    TimerTask timerTask = new TimerTask()
    {
        @Override
        public void run()
        {
            time += 1;
            if (time > 0 && time % 30 == 0)
            {
                mHandler.sendEmptyMessage(MSG_SHOW_IMAGE_AD);
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_IMAGE_AD, 10000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View rootView = View.inflate(getContext(), R.layout.hdmi_display, null);
        mBinding = DataBindingUtil.bind(rootView);
        mBinding.setP(mPresentation);
        setContentView(mBinding.getRoot());
        mBinding.videoView.getHolder().addCallback(this);
        mBinding.volumeBar.setMaxValue(15);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mPresentation.pushSurface(holder.getSurface());
//        mHandler.sendEmptyMessage(MSG_HIDE_IMAGE_AD);
        EventBus.getDefault().post(new PlayerReadyedEvent());
//        mPresentation.getTempPlayList();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        LogUtils.e("surfaceChanged");
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }


    @Override
    public void setTextAds(List<Advertisement> advertisements)
    {
        mBinding.scrollTextView.addScrollText(advertisements);
    }

    @Override
    public void setImageAds(List<Advertisement> t, List<Advertisement> b)
    {
        mBinding.rightIamge.addAds(t);
        mBinding.rightBIamge.addAds(b);
        mBinding.rightIamge.startScroll();
        mBinding.rightBIamge.startScroll();
    }


    @Subscriber
    void onPlayWhenReady(PlayWhenReadyEvent event)
    {
        if (event.playWhenReady && event.playbackState == ExoPlayer.STATE_READY)
        {
//            mBinding.statusFlag.setVisibility(View.INVISIBLE);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_HIDE_STATUS));
            mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
            if (mPlayer.getCurrentPosition() < 8000)
            {
                mBinding.txtCurrentPlay.setText(MessageFormat.format("当前播放: {0}",
                                                                     mPlayer.getMedia().getName()));

                if (mPlayer.getPlayList().isEmpty())
                {
                    mBinding.textNextPlay.setText("下首播放: 无点播曲目");
                }
                else
                {
                    mBinding.textNextPlay.setText(MessageFormat.format("下首播放: {0}",
                                                                       mPlayer.getPlayList()
                                                                              .getFirst()
                                                                              .getName()));
                }
                mBinding.textPanle.setVisibility(View.VISIBLE);
                mHandler.removeCallbacks(hideTexts);
                mHandler.postDelayed(hideTexts, 8000);
            }
        }
        else if (!event.playWhenReady && event.playbackState == ExoPlayer.STATE_READY)
        {
            mBinding.statusFlag.setVisibility(View.VISIBLE);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_SHOW_STATUS));
        }
    }

    @Subscriber
    void onService(OnServiceEvent event)
    {
        mHandler.sendEmptyMessage(MSG_SHOW_SERVICE);
    }

    @Subscriber
    void onVolumeChanged(VolumeChangedEvent event)
    {
        mBinding.muteFlag.setVisibility(event.volume == 0 ? View.VISIBLE : View.GONE);
        mBinding.volumeBar.setProgress(event.volume);
        mHandler.removeCallbacks(hideVolumeBar);
        mBinding.volumeBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(hideVolumeBar, 3000);
    }

    @Subscriber
    void onShowNextText(ShowNextEvent event)
    {
        mBinding.txtCurrentPlay.setText(MessageFormat.format("当前播放: {0}",
                                                             mPlayer.getMedia().getName()));

        if (mPlayer.getPlayList().isEmpty())
        {
            mBinding.textNextPlay.setText("即将播放: 无点播曲目");
        }
        else
        {
            mBinding.textNextPlay.setText(MessageFormat.format("即将播放: {0}",
                                                               mPlayer.getPlayList()
                                                                      .getFirst()
                                                                      .getName()));
        }
        mBinding.textPanle.setVisibility(View.VISIBLE);
    }

    private Runnable hideTexts = () -> mBinding.textPanle.setVisibility(View.INVISIBLE);

    public Bitmap screenshot()
    {
        getWindow().getDecorView().setDrawingCacheEnabled(true);
        getWindow().getDecorView().buildDrawingCache();
        return getWindow().getDecorView().getDrawingCache();
    }

    private Runnable hideVolumeBar = new Runnable()
    {
        @Override
        public void run()
        {
            mBinding.volumeBar.setVisibility(View.INVISIBLE);
        }
    };
}
