package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.support.annotation.MainThread;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.ktvdb.allen.satrok.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen on 15/7/13.
 */
public class VolumeBar extends FrameLayout
{
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.txt_volume)
    TextView    mTxtVolume;

    private int maxValue;

    private int percent;

    public int getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public int getPercent()
    {
        return percent;
    }

    public void setPercent(int percent)
    {
        this.percent = percent;
    }

    public VolumeBar(Context context)
    {
        this(context, null);
    }

    public VolumeBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VolumeBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.volume_bar, this);
        ButterKnife.bind(this);
    }

    @MainThread
    public void setProgress(int value)
    {
        mProgressBar.setProgress(value);
        float f = value / 15.f * 100;
        mTxtVolume.setText(Math.round(f) + "%");
    }
}
