package com.ktvdb.allen.satrok.service;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.NewokMedia;
import com.ktvdb.allen.satrok.model.PlayActionResult;
import com.ktvdb.allen.satrok.model.PlayRecord;
import com.ktvdb.allen.satrok.model.Song;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/9/30.
 */
public class PlayRecordService
{
    RestService service;
    //Not null, 01为歌曲点播, 02为电影点播, 03为直播(演唱会)节目收看, 04为广告播放, 05为游戏点玩
    public static final String ACTION_MEDIA_TYPE_SONG  = "01";
    public static final String ACTION_MEDIA_TYPE_MOVIE = "02";
    public static final String ACTION_MEDIA_TYPE_LIVE  = "03";
    public static final String ACTION_MEDIA_TYPE_AD    = "04";
    public static final String ACTION_MEDIA_TYPE_GAME  = "05";

//    01为点歌操作、02置顶已点(未唱)歌曲、03删除已点(未唱)歌曲、04为(节目)开始播放、05为(节目)结束播放，06为错误，07其他

    public static final String ACTION_TYPE_SELECTED = "01";
    public static final String ACTION_TYPE_TOP      = "02";
    public static final String ACTION_TYPE_DELETE   = "03";
    public static final String ACTION_TYPE_START    = "04";
    public static final String ACTION_TYPE_END      = "05";
    public static final String ACTION_TYPE_ERROR    = "06";
    public static final String ACTION_TYPE_OTHER    = "07";

    @Inject
    public PlayRecordService(RestService service)
    {
        this.service = service;
        EventBus.getDefault().register(this);
    }

    public void recordMedia(NewokMedia media, String type)
    {
        PlayRecord record = new PlayRecord.RecordBuilder()
                .playId(media.getId())
                .type(type)
                .mediaCode(media.getCode())
                .builder();

        if (media instanceof Song)
        {
            record.setActionCode(ACTION_MEDIA_TYPE_SONG);
        }
        else if (media instanceof Movie)
        {
            record.setActionCode(ACTION_MEDIA_TYPE_MOVIE);
        }
        else
        {
            record.setActionCode(ACTION_MEDIA_TYPE_LIVE);
        }

        upload(record);
    }

    public void upload(PlayRecord record)
    {
        service.addPlayAction(record)
               .observeOn(Schedulers.io())
               .subscribeOn(AndroidSchedulers.mainThread())
               .subscribe(LogUtils::e);
    }
}
