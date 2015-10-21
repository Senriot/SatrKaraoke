package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.LiveAdapter;
import com.ktvdb.allen.satrok.gui.fragment.SatelliteLiveFragment;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.view.LiveListView;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/28.
 */
@AutoInjector(MainActivity.class)
public class LivePresentation
{

    final LiveListView liveListView;
    @Inject
    RestService restService;

    LiveAdapter mAdapter;
    @Inject
    ConfigManager configManager;

    public LivePresentation(LiveListView liveListView)
    {
        this.liveListView = liveListView;
        MainActivity.getComponent().inject(this);
        mAdapter = new LiveAdapter((SatelliteLiveFragment) liveListView);
        getImageAds();
    }

    public void init()
    {
        restService.getLpTypes()
                   .observeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                   .subscribe(liveListView::updateTab);

        restService.getLives()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<LiveProgram>>empty())
                   .subscribe((livePrograms) -> {
                       if (livePrograms != null)
                       {
                           mAdapter.addAll(livePrograms);
                           liveListView.initList(mAdapter);
                       }

                   });
    }

    /**
     * 图片广告
     */
    private void getImageAds()
    {
        AppObservable.bindFragment(liveListView,
                                   restService.geLeftImageAds(configManager.getRoomInfo()
                                                                           .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(liveListView::setImageAds);
    }

    public void getAllLives()
    {
        restService.getLives().subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<LiveProgram>>empty())
                   .subscribe(livePrograms -> {
                       mAdapter.clear();
                       mAdapter.addAll(livePrograms);
                       mAdapter.notifyDataSetChanged();
                   });
    }

    public void getLiveByType(String tag)
    {
        restService.getLiveByType(tag).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<LiveProgram>>empty())
                   .subscribe(livePrograms -> {
                       mAdapter.clear();
                       mAdapter.addAll(livePrograms);
                   });
    }
}
