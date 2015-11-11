package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSongTabPageBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.gui.adapters.SingerListAdapter;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.SingerQueryCondition;
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

/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
public class SingerListPageFragment extends Fragment implements OnMoreListener
{
    FragmentSongTabPageBinding mBinding;

    GridLayoutManager mLayoutManager;

    SingerListAdapter mAdapter;

    SingerQueryCondition mCondition;

    SingerListFragment target;

    @Inject
    RestService mService;

    public static SingerListPageFragment newInstance(SingerQueryCondition condition,
                                                     SingerListFragment target)
    {
        SingerListPageFragment fragment = new SingerListPageFragment();
        fragment.mCondition = condition;
        fragment.target = target;
        return fragment;
    }

    public SingerQueryCondition getCondition()
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
        mLayoutManager = new GridLayoutManager(getActivity(),
                                               6,
                                               GridLayoutManager.VERTICAL,
                                               false);
        mBinding.list.setLayoutManager(mLayoutManager);
        mBinding.list.setupMoreListener(this, 1);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        onLoadSingers();
    }

    private void onLoadSingers()
    {
        AppObservable.bindFragment(this, mService.getSingers(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Singer>>empty())
                     .subscribe(this::notifyDatasetChanged);
    }

    private void notifyDatasetChanged(PageResponse<Singer> page)
    {
        if (mAdapter != null) mAdapter.addAll(page.getContent());
        if (page.getFirst())
        {
            mAdapter = new SingerListAdapter();
            if (target != null)
            {
                mAdapter.setOnItemClickListener(target);
            }
            mAdapter.addAll(page.getContent());
            mBinding.list.setAdapter(mAdapter);
        }
        else if (page.getLast())
        {
            mBinding.list.removeMoreListener();
        }
        mBinding.list.hideMoreProgress();
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
    {
        mCondition.setPage(mCondition.getPage() + 1);
        onLoadSingers();
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
            mCondition.setKeyword(text);
            mCondition.setPage(0);
            onLoadSingers();
            mLayoutManager.scrollToPosition(0);
        }
    }
}
