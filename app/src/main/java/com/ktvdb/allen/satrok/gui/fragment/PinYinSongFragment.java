package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.ktvdb.allen.satrok.gui.widget.KeyBoard;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.presentation.SongListPresentation;
import com.ktvdb.allen.satrok.swipe.util.Attributes;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

/**
 * A simple {@link Fragment} subclass.
 */
public class PinYinSongFragment extends SongListFragment
{

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        KeyBoard.showKeyboard(getActivity(), mPresentation, "", true);
    }


    @Override
    protected void init()
    {
        mPresentation = new SongListPresentation(this);
        mPresentation.getCondition().setSort("namesimplicity");
        mPresentation.getCondition().setDirection(Direction.ASC);
        mPresentation.getAdapter().setMode(Attributes.Mode.Single);
        mBinding.listView.setAdapter(mPresentation.getAdapter());
        mBinding.listView.getRecyclerView().setItemAnimator(new FadeInAnimator());
        mPresentation.onLoadLanguage();
    }
}
