package com.ktvdb.allen.satrok.gui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.FragmentSelectedBinding;
import com.ktvdb.allen.satrok.service.MediaPlayer;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFragment extends LevelBaseFragment<FragmentSelectedBinding> implements RadioGroup.OnCheckedChangeListener
{
    MediaPlayer mPlayer;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_selected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        replacePage(new SelectedPageFragment());
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_selected)
        {
            replacePage(new SelectedPageFragment());
        }
        else
        {
            replacePage(new PlayedPageFragment());
        }
    }

    public void replacePage(Fragment fragment)
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}
