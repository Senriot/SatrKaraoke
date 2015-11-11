package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.R;
import com.rey.material.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allen on 15/7/11.
 */
public class VolumeControlView extends FrameLayout implements SeekArc.OnSeekArcChangeListener
{
    @Bind(R.id.seekArc)
    SeekArc mSeekArc;
    @Bind(R.id.btn_jia)
    Button  mBtnJia;
    @Bind(R.id.btn_jian)
    Button  mBtnJian;


    @Bind(R.id.fan)
    FrameLayout fan;

    private int maxValue;

    public int getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }



    private ControllViewLinstener linstener;

    public void setProgress(int i)
    {
        mSeekArc.setProgress(i);
    }


    public interface ControllViewLinstener
    {
        /**
         * 已经显示
         */
        void onShowed(VolumeControlView controlView);

        /**
         * 已经隐藏
         */
        void onHided(VolumeControlView controlView);

        /**
         * 进度改变
         *
         * @param fromUser 是否用户操作
         */
        void onProgressChanged(int progress, boolean fromUser);

        /**
         * 减按钮点击
         *
         * @param button 按钮
         */
        void onReduceButtonClick(Button button, int progress);

        /**
         * 加按钮点击
         *
         * @param button 按钮
         */
        void onIncreaseButtonClick(Button button, int progress);
    }

    public VolumeControlView(Context context)
    {
        this(context, null);
    }

    public VolumeControlView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public VolumeControlView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.volume_ctrl_view, this);
        ButterKnife.bind(this);
        mSeekArc.setOnSeekArcChangeListener(this);
    }

    public ControllViewLinstener getLinstener()
    {
        return linstener;
    }

    public void setLinstener(ControllViewLinstener linstener)
    {
        this.linstener = linstener;
    }


    @OnClick({R.id.btn_jia, R.id.btn_jian})
    void onButtonClick(View view)
    {
        if (linstener == null) return;
        if (view.getId() == R.id.btn_jia)
        {
            linstener.onIncreaseButtonClick((Button) view, mSeekArc.getProgress());
        }
        else
        {
            linstener.onReduceButtonClick((Button) view, mSeekArc.getProgress());
        }
    }


    @Override
    public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser)
    {
        if (linstener != null)
        {
            linstener.onProgressChanged(progress, fromUser);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekArc seekArc)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekArc seekArc)
    {

    }
}
