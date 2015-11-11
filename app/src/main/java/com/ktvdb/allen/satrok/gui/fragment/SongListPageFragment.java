package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSongTabPageBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.service.RestService;
import com.malinskiy.superrecyclerview.OnMoreListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
public class SongListPageFragment extends Fragment implements OnMoreListener
{
    FragmentSongTabPageBinding mBinding;

    LinearLayoutManager mLayoutManager;

    SongListAdapter mAdapter;

    SongQueryCondition mCondition;

    @Inject
    RestService mService;


    public static SongListPageFragment newInstance(SongQueryCondition condition)
    {
        SongListPageFragment fragment = new SongListPageFragment();
        fragment.mCondition = condition;
        return fragment;
    }

    public SongQueryCondition getCondition()
    {
        return mCondition;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_song_tab_page,
                                           container,
                                           false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.list.setLayoutManager(mLayoutManager);
        mBinding.list.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                  DividerItemDecoration.VERTICAL_LIST));
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        onLoadSongs();
    }

    private void onLoadSongs()
    {
        AppObservable.bindFragment(this, mService.getAllSongs(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                     .subscribe(this::notifyDatasetChanged);
    }

    private void notifyDatasetChanged(PageResponse<Song> page)
    {
        if (mAdapter == null)
        {
            mAdapter = new SongListAdapter();
            mBinding.list.setAdapter(mAdapter);
        }
        if (page.getFirst())
        {
            mAdapter.clear();
        }
        if (page.getLast())
        {
            mBinding.list.removeMoreListener();
        }
        else
        {
            mBinding.list.setupMoreListener(this, 1);
        }
        mAdapter.add(page.getContent());
        mBinding.list.hideMoreProgress();
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
    {
        mCondition.setPage(mCondition.getPage() + 1);
        onLoadSongs();
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }


    public void onSearch(String text)
    {
        if (!mCondition.getKeyword().equals(text))
        {
            final String searchText = text;
            mCondition.setKeyword(searchText);
            mCondition.setPage(0);
            AppObservable.bindFragment(this, mService.getAllSongs(mCondition))
                         .cache()
                         .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                         .map(songPageResponse -> searchText.equals(mCondition.getKeyword()) ? songPageResponse : null)
                         .subscribe(songPageResponse -> {
                             if (songPageResponse != null)
                             {
                                 notifyDatasetChanged(songPageResponse);
                                 mLayoutManager.scrollToPosition(0);
                             }
                         });
        }
    }
}
