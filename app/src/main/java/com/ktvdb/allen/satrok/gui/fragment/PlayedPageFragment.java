package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSelectedPageBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.adapters.PlayedPageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.SelectedPageAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayedPageFragment extends Fragment
{
    FragmentSelectedPageBinding mBinding;
    private PlayedPageAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    public PlayedPageFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_selected_page,
                                           container,
                                           false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mBinding.list.setLayoutManager(mLayoutManager);
        mBinding.list.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                  DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new PlayedPageAdapter();
        mBinding.list.setAdapter(mAdapter);
        mBinding.list.getRecyclerView().setItemAnimator(new FadeInAnimator());
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }
}
