package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.animator.PageAnimator;
import com.fragmentmaster.app.MasterFragment;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractFragment<DB extends ViewDataBinding> extends MasterFragment
{
    @LayoutRes
    protected abstract int getLayoutId();

    DB mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        ButterKnife.bind(this, mBinding.getRoot());
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public PageAnimator onCreatePageAnimator()
    {
        return FragmnetAnimator.INSTANCE;
    }
}
