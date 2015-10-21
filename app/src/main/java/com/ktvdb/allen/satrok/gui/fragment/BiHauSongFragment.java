package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;

import com.ktvdb.allen.satrok.gui.widget.KeyBoard;

/**
 * Created by Allen on 15/9/2.
 */
public class BiHauSongFragment extends SongListFragment
{
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        KeyBoard.showKeyboard(getActivity(), mPresentation, "", false);
    }
}
