package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.presentation.view.KeGeCategoryView;
import com.ktvdb.allen.satrok.service.RestService;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/28.
 */
@AutoInjector(MainActivity.class)
public class KGeCategoryPresentation
{
    final KeGeCategoryView mView;

    @Inject
    RestService restService;

    public KGeCategoryPresentation(KeGeCategoryView mView)
    {
        this.mView = mView;
        MainActivity.getComponent().inject(this);

        restService.getRecommendAlbums().observeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<PageResponse<Album>>empty())
                   .subscribe(albumPageResponse -> {
                       mView.startScrollAlbum(albumPageResponse.getContent());
                   });
    }
}
