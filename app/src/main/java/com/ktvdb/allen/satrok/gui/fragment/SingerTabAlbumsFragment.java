package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentAlbumSongTabBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.AlbumListAdapter;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.AlbumQueryCondition;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.service.RestService;
import com.malinskiy.superrecyclerview.OnMoreListener;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;

/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
public class SingerTabAlbumsFragment extends Fragment implements OnMoreListener
{


    FragmentAlbumSongTabBinding mBinding;

    private AlbumListAdapter mAdapter;

    GridLayoutManager mLayoutManager;

    @Inject
    RestService mService;

    private Singer mSinger;

    AlbumQueryCondition mCondition = new AlbumQueryCondition();


    private OnItemClickListener itemClickListener;

    public static Fragment newInstance(Singer singer, OnItemClickListener itemClickListener)
    {
        SingerTabAlbumsFragment fragment = new SingerTabAlbumsFragment();
        Bundle                  b        = new Bundle();
        b.putSerializable(Singer.class.getName(), singer);
        fragment.setArguments(b);
        fragment.itemClickListener = itemClickListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        mLayoutManager = new GridLayoutManager(getActivity(),
                                               6,
                                               GridLayoutManager.VERTICAL,
                                               false);
        mBinding.list.setLayoutManager(mLayoutManager);
        mBinding.list.setupMoreListener(this, 1);
        if (mSinger != null)
        {
            mCondition.setSingerID(mSinger.getId());
            onLoadAlums();
        }
    }

    private void onLoadAlums()
    {
        AppObservable.bindFragment(this, mService.getAlbums(mCondition))
                     .onErrorResumeNext(Observable.<PageResponse<Album>>empty())
                     .subscribe(this::notifyDatasetChanged);
    }

    private void notifyDatasetChanged(PageResponse<Album> page)
    {
        if (mAdapter != null) mAdapter.addAll(page.getContent());
        if (page.getFirst())
        {
            mAdapter = new AlbumListAdapter();
            mAdapter.setOnItemClickListener(itemClickListener);
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
        onLoadAlums();
    }
}
