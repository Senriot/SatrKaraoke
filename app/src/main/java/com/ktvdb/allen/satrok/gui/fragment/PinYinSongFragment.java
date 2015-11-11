package com.ktvdb.allen.satrok.gui.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.gui.widget.KeyBoardDialog;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

import rx.Observable;
import rx.android.app.AppObservable;

/**
 * A simple {@link Fragment} subclass.
 */

@FragmnetTitle("拼音点歌")
public class PinYinSongFragment extends SongListBaseFragment
{
    @Override
    protected void init()
    {
        replacePage(new SongQueryCondition("namesimplicity", Direction.ASC,
                                           SongQueryCondition.SongCategory.None, null));
        KeyBoardDialog.showKeyboard(getActivity(), this, "", KeyBoardDialog.KeyboardType.PINYIN);
    }

    @Override
    protected void onLoadTabView()
    {
        AppObservable.bindFragment(this, mService.getLanguages())
                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                     .subscribe(sysDictionaries -> {
                         int id = 100;
                         for (SysDictionary tab : sysDictionaries)
                         {
                             RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                                  .inflate(R.layout.tab_radiobutton,
                                                                                           null);
                             radioButton.setText(tab.getDictName());
                             radioButton.setId(id++);
                             radioButton.setTag(tab.getId());
                             mBinding.categoryRadioGroup.addView(radioButton);
                         }
                     });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_all)
        {
            replacePage(new SongQueryCondition("namesimplicity", Direction.ASC,
                                               SongQueryCondition.SongCategory.None, null));
        }
        else
        {
            View view = group.findViewById(checkedId);
            replacePage(new SongQueryCondition("namesimplicity",
                                               Direction.ASC,
                                               SongQueryCondition.SongCategory.Language,
                                               (String) view.getTag()));
        }
    }

}
