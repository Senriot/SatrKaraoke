package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.AlbumListAdapter;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.AlbumQueryCondition;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.presentation.view.SingerDetailView;
import com.ktvdb.allen.satrok.service.RestService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * Created by Allen on 15/9/2.
 */
@AutoInjector(MainActivity.class)
public class SingerDetailPresentation
{
    private final SingerDetailView mView;
    private final Singer           mSinger;

    private SongListAdapter mSongAdapter;
    private AlbumListAdapter mAlbumAdapter = new AlbumListAdapter();

    SongQueryCondition  mSongCondition  = new SongQueryCondition();
    AlbumQueryCondition mAlbumCondition = new AlbumQueryCondition();
    PageResponse mSongPage;

    @Inject
    RestService mService;

    public SongListAdapter getSongAdapter()
    {
        return mSongAdapter;
    }

    public AlbumListAdapter getAlbumAdapter()
    {
        return mAlbumAdapter;
    }

    public SingerDetailPresentation(SingerDetailView mView, Singer mSinger)
    {
        this.mView = mView;
        this.mSinger = mSinger;
        MainActivity.getComponent().inject(this);
        EventBus.getDefault().register(this);
        mSongCondition.setCategoryID(mSinger.getId());
        mSongCondition.setSongCategory(SongQueryCondition.SongCategory.Singer);
        mAlbumAdapter.setOnItemClickListener(mView);

        AppObservable.bindFragment(mView, mService.getSinger(mSinger.getId()))
                     .onErrorResumeNext(Observable.<Singer>empty())
                     .subscribe(singer -> {
                         EventBus.getDefault().post(new BindSingerEvent(singer));
                     });

        onLoadSong(songPageResponse -> {
            mSongPage = songPageResponse;
            mSongAdapter = new SongListAdapter();
            mSongAdapter.add(songPageResponse.getContent());
            EventBus.getDefault().post(new SetSongAdapterEvent(mSongAdapter));
        });

        mAlbumCondition.setSingerID(mSinger.getId());

        onLoadAlbums();
    }

    private void onLoadSong(Action1<PageResponse<Song>> action)
    {
        AppObservable.bindFragment(mView, mService.getAllSongs(mSongCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                     .subscribe(action);
    }

    private void onLoadAlbums()
    {
        AppObservable.bindFragment(mView, mService.getAlbums(mAlbumCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Album>>empty())
                     .subscribe(albumPageResponse -> {
                         mAlbumAdapter.addAll(albumPageResponse.getContent());
                     });
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mSongAdapter.notifyDataSetChanged();
    }

    public static class SetSongAdapterEvent
    {
        public final SongListAdapter adapter;

        public SetSongAdapterEvent(SongListAdapter adapter) {this.adapter = adapter;}
    }

    public static class BindSingerEvent
    {
        public final Singer singer;

        public BindSingerEvent(Singer singer) {this.singer = singer;}
    }

}
