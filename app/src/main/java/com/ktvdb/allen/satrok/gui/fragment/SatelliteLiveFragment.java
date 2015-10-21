package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSatelliteLiveBinding;
import com.ktvdb.allen.satrok.gui.adapters.LiveAdapter;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.LivePresentation;
import com.ktvdb.allen.satrok.presentation.view.LiveListView;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SatelliteLiveFragment extends LevelBaseFragment<FragmentSatelliteLiveBinding> implements RadioGroup.OnCheckedChangeListener,
                                                                                                      LiveListView,
                                                                                                      OnItemClickListener
{
    LivePresentation mPresentation;


    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_satellite_live;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPresentation = new LivePresentation(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                                                                    5,
                                                                    GridLayoutManager.VERTICAL,
                                                                    false);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mPresentation.init();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_all)
        {
            mPresentation.getAllLives();
        }
        else
        {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            mPresentation.getLiveByType((String) radioButton.getTag());
        }
    }

    @Override
    public void setImageAds(List<Advertisement> advertisements)
    {
        mBinding.adPageView.addAds(advertisements);
        mBinding.adPageView.startScroll();
    }

    @Override
    public void updateTab(List<SysDictionary> sysDictionaries)
    {
        int id = 100;

        for (SysDictionary type : sysDictionaries)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(type.getDictName());
            radioButton.setId(id++);
            radioButton.setTag(type.getId());
            mBinding.categoryRadioGroup.addView(radioButton);
        }
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
    }


    @Override
    public void initList(LiveAdapter mAdapter)
    {
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    void showDetail(LiveProgram liveProgram)
    {
        startFragment(new Request(LiveDetailFragment.class).putExtra("title", liveProgram.getName())
                                                           .putExtra(LiveProgram.class.getName(),
                                                                     liveProgram));
    }


    @Override
    public void onItemClickListener(Object object)
    {
        LiveProgram program = (LiveProgram) object;
        startFragment(new Request(LiveDetailFragment.class).putExtra("title", program.getName())
                                                           .putExtra(LiveProgram.class.getName(),
                                                                     program));
    }
}
