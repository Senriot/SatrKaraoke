package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSingerDetailBinding;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.presentation.SingerDetailPresentation;
import com.ktvdb.allen.satrok.presentation.view.SingerDetailView;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


/**
 * Created by Allen on 15/9/4.
 */
public class SingerDetailFragment extends LevelBaseFragment<FragmentSingerDetailBinding> implements SingerDetailView,
                                                                                                    RadioGroup.OnCheckedChangeListener
{
    SingerDetailPresentation mPresentation;
    Singer                   mSinger;

    GridLayoutManager mGridLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        mSinger = (Singer) getRequest().getSerializableExtra("singer");
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_singer_detail;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mPresentation = new SingerDetailPresentation(this, mSinger);
        mGridLayoutManager = new GridLayoutManager(getActivity(),
                                                   1,
                                                   LinearLayoutManager.VERTICAL,
                                                   false);

        mBinding.recyclerView.setLayoutManager(mGridLayoutManager);
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        mBinding.singerInfo.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Subscriber
    void onSetSongAdapter(SingerDetailPresentation.SetSongAdapterEvent event)
    {
        if (mBinding.categoryRadioGroup.getCheckedRadioButtonId() != R.id.rb_album)
        {
            mBinding.recyclerView.setAdapter(event.adapter);
        }
    }

    @Subscriber
    void onBindSinger(SingerDetailPresentation.BindSingerEvent event)
    {
        mBinding.setSinger(event.singer);
    }

    @Override
    public void onItemClickListener(Object object)
    {
        if (ViewUtils.isNotDoubleClick())
        {
            Album album = (Album) object;
            startFragment(new Request(AlbumDetailFragmnet.class).putExtra(Album.class.getName(),
                                                                          album)
                                                                .putExtra("title",
                                                                          album.getTitle()));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_album)
        {
            mGridLayoutManager.setSpanCount(6);
            mBinding.recyclerView.setAdapter(mPresentation.getAlbumAdapter());
            mPresentation.getAlbumAdapter().notifyDataSetChanged();
        }
        else
        {
            mGridLayoutManager.setSpanCount(1);
            mBinding.recyclerView.setAdapter(mPresentation.getSongAdapter());
        }
    }
}
