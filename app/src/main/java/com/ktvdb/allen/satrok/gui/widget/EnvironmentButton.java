package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.EnvironmentPopupViewBinding;
import com.ktvdb.allen.satrok.databinding.LightPopupViewBinding;
import com.ktvdb.allen.satrok.gui.views.PopupButton;


/**
 * Created by Allen on 15/9/10.
 */
public class EnvironmentButton extends PopupButton implements View.OnClickListener
{
    public EnvironmentButton(Context context)
    {
        super(context);
    }

    public EnvironmentButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EnvironmentButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPopupWindow()
    {
        EnvironmentPopupViewBinding mPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()),
                                                                            R.layout.environment_popup_view,
                                                                            null,
                                                                            false);

        mPopupBinding.btnDaBa.setOnClickListener(this);
        mPopupBinding.btnHuoChe.setOnClickListener(this);
        mPopupBinding.btnJiChang.setOnClickListener(this);
        mPopupBinding.bMiss.setOnClickListener(this);
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
