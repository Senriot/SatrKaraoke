package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.presentation.SongListPresentation;

/**
 * Created by Allen on 15/9/2.
 */
public class WordCountFragmnet extends SongListFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        String[] wordCouts = getResources().getStringArray(R.array.word_count_cate);
        mBinding.categoryRadioGroup.removeAllViews();
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
        mPresentation.getCondition().setWordCount(1);
        mPresentation.getCondition().setSongCategory(SongQueryCondition.SongCategory.WordCount);

        mPresentation.onLoadSong(mPresentation::addSongs);
    }

    protected void init()
    {
        mPresentation = new SongListPresentation(this);
        mBinding.listView.setAdapter(mPresentation.getAdapter());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        mPresentation.getAdapter().clear();
        mPresentation.getCondition().setPage(0);
        mPresentation.getCondition().setWordCount(checkedId);
        mPresentation.onLoadSong(mPresentation::addSongs);
    }
}
