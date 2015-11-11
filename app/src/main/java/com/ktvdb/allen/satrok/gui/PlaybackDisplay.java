package com.ktvdb.allen.satrok.gui;

import android.app.Presentation;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.PalybackDisplayBinding;

/**
 * Created by Allen on 15/10/28.
 */
public class PlaybackDisplay extends Presentation
{

    private PalybackDisplayBinding mBinding;

    public PlaybackDisplay(Context outerContext, Display display)
    {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                           R.layout.palyback_display,
                                           null,
                                           false);
        setContentView(mBinding.getRoot());
    }
}
