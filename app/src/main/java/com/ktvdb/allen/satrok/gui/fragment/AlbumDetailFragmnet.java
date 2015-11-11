package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentAlbumDetailBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.SongListAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.Direction;
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

/**
 * Created by Allen on 15/9/4.
 */
@AutoInjector(MainActivity.class)
public class AlbumDetailFragmnet extends LevelBaseFragment<FragmentAlbumDetailBinding> implements OnMoreListener
{

    @Inject
    RestService mServie;

    private LinearLayoutManager mLayoutManager;
    private Album               mAlbum;

    private SongListAdapter mAdapter;

    SongQueryCondition mCondition = new SongQueryCondition("hot",
                                                           Direction.DESC,
                                                           SongQueryCondition.SongCategory.ALBUM,null);
    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_album_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
        mAlbum = (Album) getRequest().getSerializableExtra(Album.class.getName());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.setLayoutManager(mLayoutManager);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                          DividerItemDecoration.VERTICAL_LIST));
        mBinding.recyclerView.setupMoreListener(this, 1);

        if (mAlbum != null)
        {
            AppObservable.bindFragment(this, mServie.getAlbum(mAlbum.getId()))
                         .onErrorResumeNext(Observable.<Album>empty())
                         .subscribe(mBinding::setAlbum);
            mCondition.setCategoryID(mAlbum.getId());
        }
    }

    @Override
    public void onUserActive()
    {
        mHandler.postDelayed(this::onLoadSongs, 150);

    }

    private void onLoadSongs()
    {
        AppObservable.bindFragment(this, mServie.getAllSongs(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Song>>empty())
                     .subscribe(this::notifyDatasetChanged);
    }

    private void notifyDatasetChanged(PageResponse<Song> page)
    {
        if (mAdapter != null) mAdapter.add(page.getContent());
        if (page.getFirst())
        {
            mAdapter = new SongListAdapter();
            mAdapter.add(page.getContent());
            mBinding.recyclerView.setAdapter(mAdapter);
        }
        else if (page.getLast())
        {
            mBinding.recyclerView.setLoadingMore(false);
            mBinding.recyclerView.removeMoreListener();
        }
        mBinding.recyclerView.hideMoreProgress();
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
}
