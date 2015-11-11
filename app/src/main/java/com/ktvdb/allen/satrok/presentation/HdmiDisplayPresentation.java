package com.ktvdb.allen.satrok.presentation;

import android.view.Surface;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.event.ShowTVAdEvent;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.presentation.view.HdmiDisplayView;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.PlayCenterService;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/29.
 */
@AutoInjector(PlayCenterService.class)
public class HdmiDisplayPresentation
{

    final HdmiDisplayView mView;

    private String currentMedaiName;
    private String nextMediaName;

    @Inject
    MediaPlayer   mediaPlayer;
    @Inject
    RestService   mRestService;
    @Inject
    ConfigManager configManager;

    public HdmiDisplayPresentation(HdmiDisplayView mView)
    {
        this.mView = mView;
        PlayCenterService.component.inject(this);
        getTextAds();
        getImageAds();
    }

    public String getCurrentMedaiName()
    {
        return currentMedaiName;
    }

    public void setCurrentMedaiName(String currentMedaiName)
    {
        this.currentMedaiName = currentMedaiName;
    }

    public String getNextMediaName()
    {
        return nextMediaName;
    }

    public void setNextMediaName(String nextMediaName)
    {
        this.nextMediaName = nextMediaName;
    }

    public void pushSurface(Surface surface)
    {
        mediaPlayer.setSurface(surface);
    }


    private void getTextAds()
    {
        mRestService.getTVTextAds(configManager.getRoomInfo().getCode())
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                    .subscribe(mView::setTextAds);
    }

    /**
     * 图片广告
     */
    private void getImageAds()
    {

        mRestService.getTVImageAds(configManager.getRoomInfo().getCode())
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(advertisements -> {
                        List<Advertisement> t = new ArrayList<>();
                        List<Advertisement> b = new ArrayList<>();
                        for (Advertisement ad : advertisements)
                        {

                            if (ad.getPositionCode().equals("I_RTV_RT_200X500"))
                            {
                                t.add(ad);
                            }
                            else
                            {
                                b.add(ad);
                            }
                        }
                        mView.setImageAds(t, b);
                    });


    }

    public void getTempPlayList()
    {
        if (mediaPlayer.getTempPlayList() == null)
        {
            mRestService.getAllSongs(new SongQueryCondition("hot",
                                                            Direction.DESC,
                                                            SongQueryCondition.SongCategory.None,
                                                            null))
                        .observeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                        .subscribe(mediaPlayer::setTempPlayList);
        }

    }
}
