package com.ktvdb.allen.satrok.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.apkfuns.logutils.LogUtils;
import com.google.android.exoplayer.ExoPlayer;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.bind.PlayHubModel;
import com.ktvdb.allen.satrok.event.OnServiceEvent;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.event.PlayWhenReadyEvent;
import com.ktvdb.allen.satrok.event.VolumeChangedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.views.PopupButton;
import com.ktvdb.allen.satrok.gui.widget.VolumeControlView;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.presentation.view.MainView;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.PlayCenterService;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.TimeUtils;
import com.rey.material.widget.Button;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.IllegalFormatCodePointException;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/27.
 */
@AutoInjector(MainActivity.class)
public class MainPresentation
{
    private static final int SHOW_PROGRESS = 2;

    private final MainView mainView;

    PlayCenterService mService;
    MediaPlayer       mPlayer;


    @Inject
    ConfigManager configManager;

    @Inject
    PlayHubModel playHubModel;

    @Inject
    RestService mRestService;

    @Inject
    AudioManager audioManager;


    private String selectedCount = "0";
    private int saveVol;

    private int systemVol;


    private PopupWindow volumePopup;
    private PopupWindow micPopupWindow;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            int pos;
            switch (msg.what)
            {
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (mPlayer.isPlaying())
                    {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    private int setProgress()
    {
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        playHubModel.setMax(duration);
        playHubModel.setPosition(position);
        playHubModel.setEndTime(TimeUtils.stringForTime(duration));
        playHubModel.setCurrentTime(TimeUtils.stringForTime(position));
        return position;
    }

    /**
     * 全局搜索蚊子
     */
    private String searchText;

    private int channel;

    public MainPresentation(MainView mainView)
    {
        this.mainView = mainView;
        MainActivity.getComponent().inject(this);
        EventBus.getDefault().register(this);
        getTextAds();
    }

    public String getRoomNo()
    {
        return configManager.getRoomInfo().getCode();
    }

    public PlayCenterService getService()
    {
        return mService;
    }

    public void setService(PlayCenterService mService)
    {
        this.mService = mService;
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
    }

    public String getSelectedCount()
    {
        return selectedCount;
    }

    public String getSearchText()
    {
        return searchText;
    }

    public void setSearchText(String searchText)
    {
        this.searchText = searchText;
        LogUtils.e(searchText);
    }


    /**
     * 滚动字幕
     */
    private void getTextAds()
    {
        mRestService.getMainTextAds(configManager.getRoomInfo().getCode())
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                    .subscribe(mainView::setTextAds);
    }

    /**
     * 请求服务
     *
     * @param view
     */
    public void onService(View view)
    {
        EventBus.getDefault().post(new OnServiceEvent());
        Bitmap bitmap = mService.bitmap();
        mainView.showBitmap(bitmap);
    }


    /**
     * 暂停或播放
     *
     * @param view 按钮
     */
    public void onPauseOrPlay(View view)
    {
        if (mPlayer != null)
        {
            PopupButton button = (PopupButton) view;
            if (mPlayer.isPlaying())
            {
                mPlayer.pause();
                button.setTitle("播放");
                button.setIconRes(R.drawable.ic_play);

            }
            else
            {
                mPlayer.start();
                button.setTitle("暂停");
                button.setIconRes(R.drawable.ic_zanting);
            }
        }

    }

    /**
     * 重唱
     *
     * @param view 按钮
     */
    public void onReplay(View view)
    {
        if (mPlayer != null)
        {
            mPlayer.rePlay();
        }
    }

    /**
     * 静音
     *
     * @param view
     */
    public void onMute(View view)
    {
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (vol == 0)
        {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, saveVol, 0);
        }
        else
        {
            saveVol = vol;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        }
    }


    /**
     * 切换音轨
     *
     * @param view 按钮
     */
    public void onChangeAudioTrack(View view)
    {
        int track = mPlayer.getAudioTrackCount();

        if (track <= 1)
        {
            if (channel == 0)
            {
                mPlayer.setChannel(3);
                channel = 3;
            }
            else
            {
                if (channel == 3)
                {
                    mPlayer.setChannel(4);
                    channel = 4;
                    configManager.setYuanBanFlag(ConfigManager.FLAG_YUAN_CHANG);
                }
                else
                {
                    mPlayer.setChannel(3);
                    channel = 3;
                    configManager.setYuanBanFlag(ConfigManager.FLAG_BAN_CHANG);
                }

            }
        }
        else
        {
            int selected = mPlayer.getSelectedTrack();
            if (selected == 0)
            {
                mPlayer.setSelectedTrack(1);
                configManager.setYuanBanFlag(ConfigManager.FLAG_BAN_CHANG);
            }
            else
            {
                mPlayer.setSelectedTrack(0);
                configManager.setYuanBanFlag(ConfigManager.FLAG_YUAN_CHANG);
            }
        }
    }


    public void saveChannel()
    {
        int track = mPlayer.getAudioTrackCount();
        if (track <= 1)
        {
            mPlayer.setChannel(channel);
        }
    }


    public void onCut(View view)
    {
        mPlayer.onCut();
    }

    @Subscriber
    void onPlayWhenReady(PlayWhenReadyEvent event)
    {
        if (event.playWhenReady && event.playbackState == ExoPlayer.STATE_READY)
        {
            if (mPlayer.getAudioTrackCount() <= 1)
            {
                mPlayer.setSelectedTrack(0);
            }
            else
            {
                if (configManager.getYuanBanFlag() == ConfigManager.FLAG_BAN_CHANG)
                {
                    mPlayer.setSelectedTrack(1);
                }
                else
                {
                    mPlayer.setSelectedTrack(0);
                }
            }
            playHubModel.setSongName(mPlayer.getMedia().getName());
            if (mPlayer.getMedia() instanceof Song)
            {
                playHubModel.setSingerName(((Song) mPlayer.getMedia()).getSingerNames());
                String imageUrl = configManager.getSongImageUrl(mPlayer.getMedia()
                                                                       .getId() + "/200/200");
                playHubModel.setImageUrl(imageUrl);
                saveChannel();
            }
            else
            {
                playHubModel.setSingerName("");
            }
            playHubModel.setSelectedCount("" + mPlayer.getPlayList().size());
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
        else if (event.playWhenReady && event.playbackState == ExoPlayer.STATE_IDLE)
        {

        }
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        playHubModel.setSelectedCount("" + mPlayer.getPlayList().size());
    }

    @Subscriber
    void onVolumeChanged(VolumeChangedEvent event)
    {
        systemVol = event.volume;
        mainView.setMute(systemVol == 0);

    }

    /**
     * 显示音量调节
     *
     * @param view
     */
    public void onShowVolume(View view)
    {
        if (volumePopup == null)
        {
            VolumeControlView volumeControlView = new VolumeControlView((Context) mainView);

            volumePopup = new PopupWindow((Context) mainView);
            volumePopup.setFocusable(true);
            volumePopup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            volumePopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            volumePopup.setContentView(volumeControlView);
            volumePopup.setBackgroundDrawable(new BitmapDrawable());
            volumePopup.update();
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            volumeControlView.measure(width, height);
            volumeControlView.setLinstener(new VolumeControlView.ControllViewLinstener()
            {
                @Override
                public void onShowed(VolumeControlView controlView)
                {

                }

                @Override
                public void onHided(VolumeControlView controlView)
                {

                }

                @Override
                public void onProgressChanged(int progress, boolean fromUser)
                {
                    LogUtils.e(progress);
                    if (fromUser)
                    {

                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    }
                }

                @Override
                public void onReduceButtonClick(Button button, int progress)
                {
                    volumeControlView.setProgress(progress - 1);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress - 1, 0);
                }

                @Override
                public void onIncreaseButtonClick(Button button, int progress)
                {
                    volumeControlView.setProgress(progress + 1);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress + 1, 0);
                }
            });
        }
        int height = volumePopup.getContentView().getMeasuredHeight();
        int width  = volumePopup.getContentView().getMeasuredWidth();
        volumePopup.showAsDropDown(view, view.getWidth() / 2 - width / 2, -height);
        VolumeControlView volumeControlView = (VolumeControlView) volumePopup.getContentView();
        volumeControlView.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    /**
     * 显示麦克音量调节
     *
     * @param view
     */
    public void onShowMicrophone(View view)
    {
        if (micPopupWindow == null)
        {
            VolumeControlView volumeControlView = new VolumeControlView((Context) mainView);

            micPopupWindow = new PopupWindow((Context) mainView);
            micPopupWindow.setFocusable(true);
            micPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            micPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            micPopupWindow.setContentView(volumeControlView);
            micPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            micPopupWindow.update();
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            volumeControlView.measure(width, height);
            volumeControlView.setLinstener(new VolumeControlView.ControllViewLinstener()
            {
                @Override
                public void onShowed(VolumeControlView controlView)
                {

                }

                @Override
                public void onHided(VolumeControlView controlView)
                {

                }

                @Override
                public void onProgressChanged(int progress, boolean fromUser)
                {
                    LogUtils.e(progress);
                }

                @Override
                public void onReduceButtonClick(Button button, int progress)
                {

                }

                @Override
                public void onIncreaseButtonClick(Button button, int progress)
                {

                }
            });
        }
        int height = micPopupWindow.getContentView().getMeasuredHeight();
        int width  = micPopupWindow.getContentView().getMeasuredWidth();
        micPopupWindow.showAsDropDown(view, view.getWidth() / 2 - width / 2, -height);
    }
}
