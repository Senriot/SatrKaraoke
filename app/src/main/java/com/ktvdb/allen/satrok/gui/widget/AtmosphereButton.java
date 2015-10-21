package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.AtmospherePopupViewBinding;
import com.ktvdb.allen.satrok.gui.views.PopupButton;
import com.ktvdb.allen.satrok.service.MediaPlayer;

import java.io.IOException;


/**
 * Created by Allen on 15/9/10.
 */
public class AtmosphereButton extends PopupButton implements View.OnClickListener
{
    private MediaPlayer mPlayer;

    public AtmosphereButton(Context context)
    {
        super(context);
    }

    public AtmosphereButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AtmosphereButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPopupWindow()
    {

        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        AtmospherePopupViewBinding mPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(
                                                                                   getContext()),
                                                                           R.layout.atmosphere_popup_view,
                                                                           null,
                                                                           false);

        mPopupBinding.btnGuZhang.setOnClickListener(this);
        mPopupBinding.btnHuanHu.setOnClickListener(this);
        mPopupBinding.btnDaoCai.setOnClickListener(this);
        mPopupBinding.bMiss.setOnClickListener(this);
        mPopupBinding.btnKouShao.setOnClickListener(this);
        mPopupWindow = new PopupWindow(getContext());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(mPopupBinding.getRoot());
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.update();

        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        mPopupBinding.getRoot().measure(width, height);

        popupheight = mPopupBinding.getRoot().getMeasuredHeight();

        popupWidth = mPopupBinding.getRoot().getMeasuredWidth();
    }

    @Override
    public void onClick(View v)
    {
        mPopupWindow.dismiss();
        AssetManager        assetManager   = v.getContext().getAssets();
        AssetFileDescriptor fileDescriptor = null;
        try
        {
            switch (v.getId())
            {
                case R.id.btnDaoCai:
                    fileDescriptor = assetManager.openFd("daocai.mp3");
                    break;
                case R.id.btnGuZhang:
                    fileDescriptor = assetManager.openFd("guzhang.mp3");
                    break;
                case R.id.btnHuanHu:
                    fileDescriptor = assetManager.openFd("huanhu.mp3");
                    break;
                case R.id.btnKouShao:
                    fileDescriptor = assetManager.openFd("koushao.mp3");
                    break;
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (fileDescriptor != null)
        {
            mPlayer.playLocalMp3(fileDescriptor);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            mPopupWindow.showAsDropDown(mBinding.icon,
                                        this.getWidth() / 2 - popupWidth / 2,
                                        -popupheight + 20);
        }

        return false;
    }
}
