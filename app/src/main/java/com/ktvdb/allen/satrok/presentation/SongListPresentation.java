package com.ktvdb.allen.satrok.presentation;

import android.view.View;

import com.ktvdb.allen.satrok.DaggerScope;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.gui.widget.KeyBoard;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.module.SongListModule;
import com.ktvdb.allen.satrok.presentation.view.SongListFragmentView;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.RecyclerUtils;

import org.apache.commons.lang3.StringUtils;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/30.
 */
//@AutoInjector(PlayCenterService.class)
@AutoComponent(
        modules = {SongListModule.class},
        dependencies = {StarokApplication.class}
)
@AutoInjector
@DaggerScope(SongListPresentation.class)
public class SongListPresentation implements KeyBoard.OnTextChangedLinstener,
                                             RecyclerUtils.RecyclerItemClickListener.OnItemClickListener
{
    private final SongListFragmentView mView;

    @Inject
    RestService restService;

    @Inject
    SongQueryCondition mCondition;

    @Inject
    MediaPlayer mPlayer;

    @Inject
    ConfigManager configManager;

    SongListAdapter mAdapter = new SongListAdapter();


    public SongQueryCondition getCondition()
    {
        return mCondition;
    }


    public SongListAdapter getAdapter()
    {
        return mAdapter;
    }

    public SongListPresentation(SongListFragmentView mView)
    {
        this.mView = mView;
        SongListPresentationComponent component = DaggerSongListPresentationComponent.builder()
                                                                                     .starokApplicationComponent(
                                                                                             StarokApplication
                                                                                                     .getAppContext()
                                                                                                     .getComponent())
                                                                                     .songListModule(
                                                                                             new SongListModule())
                                                                                     .build();
        component.inject(this);
        mCondition.setDirection(Direction.DESC);
        mCondition.setSort("hot");
        EventBus.getDefault().register(this);

        getImageAds();
    }

    /**
     * 图片广告
     */
    private void getImageAds()
    {
        AppObservable.bindFragment(mView,
                                   restService.geLeftImageAds(configManager.getRoomInfo()
                                                                           .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(mView::setImageAds);
    }

    /**
     * 加载语种
     */
    public void onLoadLanguage()
    {
        restService.getLanguages().subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                   .subscribe(mView::updateTabView);
    }

    public void onLoadSong(Action1<PageResponse<Song>> action)
    {
        restService.getAllSongs(mCondition)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                   .subscribe(action);

    }

    public void initSongs()
    {
        onLoadSong(this::addSongs);
    }

    public void onMore()
    {
        mCondition.setPage(mCondition.getPage() + 1);
        onLoadSong(this::addSongs);
    }

    public void addSongs(PageResponse<Song> page)
    {
        mAdapter.add(page.getContent());
        mView.onLoadcompled(page);
    }

    public void reLoadSong(String tag)
    {
        mAdapter.clear();
        mCondition.setKeyword("");
        if (StringUtils.isBlank(tag))
        {
            mCondition.setSongCategory(SongQueryCondition.SongCategory.None);
            mCondition.setCategoryID(null);
        }
        else
        {
            mCondition.setSongCategory(SongQueryCondition.SongCategory.Language);
            mCondition.setCategoryID(tag);
        }

        mCondition.setPage(0);
        onLoadSong(this::addSongs);
    }

    public void reloadCateSongs(String cate)
    {
        mAdapter.clear();
        mCondition.setKeyword("");
        mCondition.setCategoryID(cate);
        mCondition.setPage(0);
        onLoadSong(this::addSongs);
    }

    public void getSongCategory(String id)
    {
        AppObservable.bindFragment(mView, restService.getSongCategory(id))
                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                     .subscribe(mView::updateTabView);
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTextChanged(String text)
    {
        if (!mCondition.getKeyword().equals(text))
        {
            mCondition.setKeyword(text);
            onLoadSong(songPageResponse -> {
                mAdapter.clear();
                mCondition.setPage(0);
                addSongs(songPageResponse);
            });
        }
    }

    @Override
    public void onItemClick(View view, int position)
    {
        mPlayer.addLastMedia(mAdapter.getItem(position));
    }
}
