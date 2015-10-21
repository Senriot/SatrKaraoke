package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.service.RestService;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/9/4.
 */
@AutoInjector(MainActivity.class)
public class AlbumDetailPresentation
{
    private final Album album;

    private SongListAdapter mAdapter;


    @Inject
    RestService mServie;

    public AlbumDetailPresentation(Album album)
    {
        this.album = album;
        MainActivity.getComponent().inject(this);

        mServie.getAlbum(album.getId())
               .subscribeOn(AndroidSchedulers.mainThread())
               .observeOn(Schedulers.io())
               .onErrorResumeNext(Observable.<Album>empty())
               .subscribe(album1 -> {
                   EventBus.getDefault().post(new BindAlbumEvent(album1));
               });
    }


    private void onLoadSong()
    {
    }

    public static class BindAlbumEvent
    {
        public final Album album;

        public BindAlbumEvent(Album album) {this.album = album;}
    }
}
