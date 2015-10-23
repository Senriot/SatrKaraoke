package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSongListBinding;
import com.ktvdb.allen.satrok.gui.adapters.AdvertisementImageAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.gui.widget.KeyBoard;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.SongListPresentation;
import com.ktvdb.allen.satrok.presentation.view.SongListFragmentView;
import com.ktvdb.allen.satrok.swipe.util.Attributes;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends LevelBaseFragment<FragmentSongListBinding> implements SongListFragmentView,
                                                                                            RadioGroup.OnCheckedChangeListener,
                                                                                            OnMoreListener
{
    SongListPresentation mPresentation;

    WindowManager mWindowManager;

    AdvertisementImageAdapter advertisementImageAdapter;

    LinearLayoutManager mLayoutManager;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_song_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        mLayoutManager = new LinearLayoutManager(getActivity());
        final SuperRecyclerView recyclerView = mBinding.listView;
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                 DividerItemDecoration.VERTICAL_LIST));
        init();
    }

    protected void init()
    {
        mPresentation = new SongListPresentation(this);
        mPresentation.getAdapter().setMode(Attributes.Mode.Single);
        mBinding.listView.setAdapter(mPresentation.getAdapter());
        mBinding.listView.getRecyclerView().setItemAnimator(new FadeInAnimator());
        mPresentation.onLoadLanguage();
    }

    @Override
    public void updateTabView(List<SysDictionary> tabs)
    {
        int id = 100;
        for (SysDictionary tab : tabs)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(tab.getDictName());
            radioButton.setId(id++);
            radioButton.setTag(tab.getId());
            mBinding.categoryRadioGroup.addView(radioButton);
        }
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        mPresentation.initSongs();
    }

    @Override
    public void onLoadcompled(PageResponse page)
    {
        mBinding.listView.hideMoreProgress();
        if (page.getFirst())
        {
            mLayoutManager.scrollToPosition(0);
        }
        if (page.getLast())
        {
            mBinding.listView.setLoadingMore(false);
            mBinding.listView.removeMoreListener();
        }
        else
        {
            mBinding.listView.setupMoreListener(this, 1);
        }
    }

    @Override
    public void setImageAds(List<Advertisement> advertisements)
    {
        advertisementImageAdapter = new AdvertisementImageAdapter(advertisements, getActivity());
        mBinding.adPageView.addAds(advertisements);
        mBinding.adPageView.startScroll();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_all)
        {
            mPresentation.reLoadSong(null);
        }
        else
        {
            View view = group.findViewById(checkedId);

            mPresentation.reLoadSong((String) view.getTag());
        }
    }

    @Override
    public void onMoreAsked(int i, int i1, int i2)
    {
        mPresentation.onMore();
    }

    @OnClick(R.id.btnKeyboard)
    public void showKeyboard()
    {
        KeyBoard.showKeyboard(getActivity(),
                              mPresentation,
                              mPresentation.getCondition().getKeyword());
    }
}
