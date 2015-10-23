package com.ktvdb.allen.satrok.gui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListPageFragment extends Fragment
{


    public SongListPageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_list_page, container, false);
    }


}
