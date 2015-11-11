package com.ktvdb.allen.satrok.gui.fragment;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.SongQueryCondition;

/**
 * Created by Allen on 15/9/2.
 */
@FragmnetTitle("字数点歌")
public class WordCountFragmnet extends SongListBaseFragment
{
    @Override
    protected void init()
    {
    }

    @Override
    protected void onLoadTabView()
    {
        mBinding.categoryRadioGroup.removeAllViews();
        String[] wordCouts = getResources().getStringArray(R.array.word_count_cate);
        for (int i = 0; i < wordCouts.length; i++)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(wordCouts[i]);
            radioButton.setId(i + 1);
            mBinding.categoryRadioGroup.addView(radioButton);
            if (i == 0) radioButton.setChecked(true);
        }
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        SongQueryCondition condition = new SongQueryCondition("hot",
                                                              Direction.DESC,
                                                              SongQueryCondition.SongCategory.WordCount,
                                                              null);
        condition.setWordCount(checkedId);
        replacePage(condition);
    }
}
