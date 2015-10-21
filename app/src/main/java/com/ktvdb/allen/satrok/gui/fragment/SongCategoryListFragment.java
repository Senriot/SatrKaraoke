package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.SongListPresentation;

import java.util.List;

import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongCategoryListFragment extends SongListFragment
{

    String id;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        id = getRequest().getStringExtra("categoryID");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.categoryRadioGroup.removeAllViews();
//        mPresentation.getCondition().setSongCategory(SongQueryCondition.SongCategory.valueOf(id));

//        mCondition.setSongCategory(SongQueryCondition.SongCategory.valueOf(id));
//        mCategoryRadioGroup.setOnCheckedChangeListener(this);
//        mCategoryRadioGroup.removeAllViews();
//        AppObservable.bindFragment(this, restService.getSongCategory(id))
//                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
//                     .subscribe(dictionaries -> {
//                         updateRadioGroup(dictionaries, true);
//                     });
    }

    @Override
    protected void init()
    {
        mPresentation = new SongListPresentation(this);
        mBinding.listView.setAdapter(mPresentation.getAdapter());
        mPresentation.getCondition().setSongCategory(SongQueryCondition.SongCategory.valueOf(id));
        mPresentation.getSongCategory(id);
    }

    @Override
    public void updateTabView(List<SysDictionary> tabs)
    {
        super.updateTabView(tabs);
        if (!tabs.isEmpty())
        {
            RadioButton radioButton = (RadioButton) mBinding.categoryRadioGroup
                    .findViewWithTag(tabs.get(0).getId());
            radioButton.setChecked(true);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
//        super.onCheckedChanged(group, checkedId);
        View view = group.findViewById(checkedId);
        mPresentation.reloadCateSongs((String) view.getTag());
//        mPresentation.getCondition().setCategoryID((String) view.getTag());
//        mPresentation.getCondition().setPage(0);
//        mPresentation.getAdapter().clear();
//        mPresentation.onLoadSong(songPageResponse -> mPresentation.getAdapter().add(songPageResponse.getContent()));
//
//        onLoadSong();
    }
}
