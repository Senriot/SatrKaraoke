package com.ktvdb.allen.satrok.gui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSingerListBinding;
import com.ktvdb.allen.satrok.gui.adapters.AdvertisementImageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.SingerListAdapter;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.gui.widget.KeyBoard;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.SingerListPresentation;
import com.ktvdb.allen.satrok.presentation.view.SingerListView;
import com.malinskiy.superrecyclerview.OnMoreListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class SingerListFragment extends LevelBaseFragment<FragmentSingerListBinding> implements SingerListView,
                                                                                                OnMoreListener,
                                                                                                RadioGroup.OnCheckedChangeListener
{

    SingerListPresentation mPresentation;
    private AdvertisementImageAdapter advertisementImageAdapter;

    public SingerListFragment()
    {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_singer_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mPresentation = new SingerListPresentation(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                                                                    6,
                                                                    GridLayoutManager.VERTICAL,
                                                                    false);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onMoreAsked(int i, int i1, int i2)
    {
        mPresentation.loadMore();
    }


    @Override
    public void updateTabView(List<SysDictionary> sysDictionaries)
    {
        int id = 100;
        for (SysDictionary type : sysDictionaries)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(type.getDictName());
            radioButton.setTag(type.getId());
            radioButton.setId(id++);
            mBinding.categoryRadioGroup.addView(radioButton);

        }
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void setAdapter(SingerListAdapter mAdapter)
    {
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void refresh(boolean hsMore)
    {
        mBinding.recyclerView.hideMoreProgress();
        if (hsMore)
        {
            mBinding.recyclerView.setLoadingMore(false);
            mBinding.recyclerView.removeMoreListener();
        }
        else
        {
            mBinding.recyclerView.setupMoreListener(this, 1);
        }
    }

    @Override
    public void setImageAds(List<Advertisement> advertisements)
    {
        mBinding.adImagePage.addAds(advertisements);
        mBinding.adImagePage.startScroll();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        RadioButton button = (RadioButton) group.findViewById(checkedId);
        mPresentation.getSingerByType(checkedId == R.id.rb_all ? null : (String) button.getTag());
    }


    @Override
    public void onItemClickListener(Object object)
    {
        Singer singer = (Singer) object;
        startFragment(new Request(SingerDetailFragment.class)
                              .putExtra("singer", singer)
                              .putExtra("title", singer.getSimpName()));
    }

    @OnClick(R.id.btnKeyboard)
    public void showKeyboard()
    {
        KeyBoard.showKeyboard(getActivity(),
                              mPresentation,
                              mPresentation.getCondition().getKeyword());
    }
}
