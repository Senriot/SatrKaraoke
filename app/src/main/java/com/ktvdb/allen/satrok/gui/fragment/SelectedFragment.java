package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.adapters.SelectedAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFragment extends LevelBaseFragment implements RadioGroup.OnCheckedChangeListener
{
    SelectedAdapter mAdapter;
    MediaPlayer     mPlayer;
    @Bind(R.id.category_radioGroup)
    RadioGroup        mCategoryRadioGroup;
    @Bind(R.id.recyclerView)
    SuperRecyclerView mRecyclerView;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_selected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                                                                1,
                                                                LinearLayoutManager.VERTICAL,
                                                                false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                  DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new SelectedAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(mPlayer.getPlayList());
        mCategoryRadioGroup.setOnCheckedChangeListener(this);
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_selected)
        {
            mAdapter.setPlayed(false);
            mAdapter.setList(mPlayer.getPlayList());
        }
        else
        {
            mAdapter.setPlayed(true);
            mAdapter.setList(mPlayer.getPlayedList());
        }
    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }
}
