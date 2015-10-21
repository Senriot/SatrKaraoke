package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSelectedPageBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedPageFragment extends Fragment
{
    FragmentSelectedPageBinding mBinding;

    public SelectedPageFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater,
                                           R.layout.fragment_selected_page,
                                           container,
                                           false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
}
