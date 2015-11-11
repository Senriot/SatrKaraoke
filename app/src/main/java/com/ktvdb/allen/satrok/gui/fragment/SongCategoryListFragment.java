package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

import rx.Observable;
import rx.android.app.AppObservable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongCategoryListFragment extends SongListBaseFragment
{
    String id;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        id = getRequest().getStringExtra("categoryID");
    }

    @Override
    protected void init()
    {

    }

    @Override
    protected void onLoadTabView()
    {
        mBinding.categoryRadioGroup.removeAllViews();
        AppObservable.bindFragment(this, mService.getSongCategory(id))
                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                     .subscribe(sysDictionaries -> {
                         int id1 = 100;
                         for (SysDictionary tab : sysDictionaries)
                         {
                             RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                                  .inflate(R.layout.tab_radiobutton,
                                                                                           null);
                             radioButton.setText(tab.getDictName());
                             radioButton.setId(id1++);
                             radioButton.setTag(tab.getId());
                             mBinding.categoryRadioGroup.addView(radioButton);
                         }
                         RadioButton radioButton = (RadioButton) mBinding.categoryRadioGroup
                                 .findViewWithTag(sysDictionaries.get(0).getId());
                         radioButton.setChecked(true);
                     });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        View view = group.findViewById(checkedId);
        SongQueryCondition condition = new SongQueryCondition("hot", Direction.DESC,
                                                              SongQueryCondition.SongCategory.valueOf(
                                                                      id), (String) view.getTag());
        replacePage(condition);
    }
}
