package com.ktvdb.allen.satrok.presentation;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.presentation.view.MainPageView;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/27.
 */
@AutoInjector(MainActivity.class)
public class MainPagePresentation
{
    final MainPageView mainPageView;

    @Inject
    ConfigManager configManager;
    @Inject
    RestService   restService;

    public MainPagePresentation(MainPageView mainPageView)
    {
        this.mainPageView = mainPageView;
        MainActivity.getComponent().inject(this);
        getRecommendAlbums(mainPageView);
        getImageAds();
    }

    private void getRecommendAlbums(MainPageView mainPageView)
    {
        AppObservable.bindFragment(mainPageView,
                                   restService.getRecommendAlbums())
                     .onErrorResumeNext(Observable.<PageResponse<Album>>empty())
                     .subscribe(albumPageResponse -> {
                         if (!albumPageResponse.getContent().isEmpty())
                         {
                             mainPageView.onPlayRecommendAlbums(albumPageResponse.getContent());
                         }
                     });
    }


    /**
     * 图片广告
     */
    private void getImageAds()
    {
        AppObservable.bindFragment(mainPageView,
                                   restService.getHomeImageAds(configManager.getRoomInfo()
                                                                            .getCode()))
//                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(mainPageView::setImageAds);
    }
}
