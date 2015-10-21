package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentAlbumDetailBinding;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.presentation.AlbumDetailPresentation;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by Allen on 15/9/4.
 */
public class AlbumDetailFragmnet extends LevelBaseFragment<FragmentAlbumDetailBinding>
{
    private AlbumDetailPresentation mPresentation;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_album_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),
                                                                 1,
                                                                 LinearLayoutManager.VERTICAL,
                                                                 false);
        mBinding.recyclerView.setLayoutManager(mLayoutManager);
        Album mAlbum = (Album) getRequest().getSerializableExtra(Album.class.getName());
        mPresentation = new AlbumDetailPresentation(mAlbum);
    }

    @Subscriber
    void onBindAlbum(AlbumDetailPresentation.BindAlbumEvent event)
    {
        mBinding.setAlbum(event.album);
    }
}
