package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentAlbumSongTabBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Singer;
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


/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
public class SingerTabSongsFragment extends Fragment implements OnMoreListener
{

    FragmentAlbumSongTabBinding mBinding;

    private SongListAdapter mSongAdapter;

    LinearLayoutManager mLayoutManager;

    @Inject
    RestService mService;

    SongQueryCondition mCondition = new SongQueryCondition("hot",
                                                           Direction.DESC,
                                                           SongQueryCondition.SongCategory.Singer,null);


    private Singer mSinger;

    public static Fragment newInstance(Singer singer)
    {
        SingerTabSongsFragment fragment = new SingerTabSongsFragment();
        Bundle                 b        = new Bundle();
        b.putSerializable(Singer.class.getName(), singer);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        MainActivity.getComponent().inject(this);
        mSinger = (Singer) getArguments().getSerializable(Singer.class.getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_album_song_tab,
                                           container,
                                           false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.list.setLayoutManager(mLayoutManager);
        mBinding.list.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                  DividerItemDecoration.VERTICAL_LIST));
        mBinding.list.setupMoreListener(this, 1);

        if (mSinger != null)
        {
            mCondition.setCategoryID(mSinger.getId());
            onLoadSong();
        }
    }


    private void onLoadSong()
    {
        AppObservable.bindFragment(this, mService.getAllSongs(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                     .subscribe(this::notifyDatasetChanged);
    }

    private void notifyDatasetChanged(PageResponse<Song> page)
    {
        if (mSongAdapter != null) mSongAdapter.add(page.getContent());
        if (page.getFirst())
        {
            mSongAdapter = new SongListAdapter();
            mSongAdapter.add(page.getContent());
            mBinding.list.setAdapter(mSongAdapter);
        }
        else if (page.getLast())
        {
            mBinding.list.setLoadingMore(false);
            mBinding.list.removeMoreListener();
        }
        mBinding.list.hideMoreProgress();
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition)
    {
        mCondition.setPage(mCondition.getPage() + 1);
        onLoadSong();
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mSongAdapter.notifyDataSetChanged();
    }

}


