package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.SingerListAdapter;
import com.ktvdb.allen.satrok.gui.widget.KeyBoard;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.SingerQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.view.SingerListView;
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
 * Created by Allen on 15/9/2.
 */
@AutoInjector(MainActivity.class)
public class SingerListPresentation implements KeyBoard.OnTextChangedLinstener
{
    final SingerListView mView;

    @Inject
    RestService mRestService;

    SingerListAdapter mAdapter;

    SingerQueryCondition mCondition;
    PageResponse         mPage;

    @Inject
    ConfigManager configManager;

    public SingerQueryCondition getCondition()
    {
        return mCondition;
    }

    public SingerListPresentation(SingerListView mView)
    {
        this.mView = mView;
        MainActivity.getComponent().inject(this);
        getSingerTypes();
        mCondition = new SingerQueryCondition();
        mCondition.setSort("hot");
        mCondition.setDirection(Direction.DESC);

        onLoadSonger(singerPageResponse -> {
            mAdapter = new SingerListAdapter();
            mAdapter.setOnItemClickListener(mView);
            mView.setAdapter(mAdapter);
            addSongs(singerPageResponse);
        });

        getImageAds();
    }

    /**
     * 图片广告
     */
    private void getImageAds()
    {
        AppObservable.bindFragment(mView,
                                   mRestService.geLeftImageAds(configManager.getRoomInfo()
                                                                            .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(mView::setImageAds);
    }


    public void getSingerTypes()
    {
        mRestService.getSingerType().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                    .subscribe(mView::updateTabView);
    }

    public void onLoadSonger(Action1<PageResponse<Singer>> action)
    {
        AppObservable.bindFragment(mView, mRestService.getSingers(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Singer>>empty())
                     .subscribe(action);
    }

    public void getSingerByType(String s)
    {
        mCondition.setType(s);
        mCondition.setSingerCategory(s == null ? SingerQueryCondition.SingerCategory.None
                                             : SingerQueryCondition.SingerCategory.SingerType);
        mAdapter.clear();
        mCondition.setPage(0);
        mCondition.setKeyword("");
        onLoadSonger(this::addSongs);
    }

    private void addSongs(PageResponse<Singer> page)
    {
        mPage = page;
        mAdapter.addAll(page.getContent());
        mView.refresh(page.getLast());
    }

    public void loadMore()
    {
        mCondition.setPage(mPage.getNumber() + 1);
        onLoadSonger(this::addSongs);
    }

    @Override
    public void onTextChanged(String text)
    {
        if (!mCondition.getKeyword().equals(text))
        {
            mCondition.setKeyword(text);
            onLoadSonger(songPageResponse -> {
                mAdapter.clear();
                mCondition.setPage(0);
                addSongs(songPageResponse);
            });
        }
    }
}
