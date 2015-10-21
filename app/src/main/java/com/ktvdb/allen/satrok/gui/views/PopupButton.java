package com.ktvdb.allen.satrok.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.NewPopupButtonBinding;
import com.ktvdb.allen.satrok.databinding.PopupbuttonPopupBinding;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Allen on 15/9/8.
 */
public class PopupButton extends FrameLayout
{

    protected NewPopupButtonBinding mBinding;
    PopupbuttonPopupBinding mPopupBinding;

    protected PopupWindow mPopupWindow;

    protected int popupWidth;
    protected int popupheight;

    private int    iconRes;
    private String title;
    private int    iconBackgroud;
    private int    popupBackground;

    public int getIconBackgroud()
    {
        return iconBackgroud;
    }

    public void setIconBackgroud(int iconBackgroud)
    {
        this.iconBackgroud = iconBackgroud;
    }

    public int getPopupBackground()
    {
        return popupBackground;
    }

    public void setPopupBackground(int popupBackground)
    {
        this.popupBackground = popupBackground;
    }

    public int getIconRes()
    {
        return iconRes;
    }

    public void setIconRes(int iconRes)
    {
        this.iconRes = iconRes;
        if (iconRes != 0)
        {
            mBinding.icon.setImageResource(iconRes);
            mPopupBinding.icImage.setImageResource(iconRes);
        }
    }

    public void setIconBackground(int iconBackgroud)
    {
        mBinding.icon.setBackgroundResource(iconBackgroud);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
        mBinding.txtName.setText(title);
    }

    public PopupButton(Context context)
    {
        this(context, null);
    }

    public PopupButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public PopupButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                           R.layout.new_popup_button,
                                           this,
                                           true);
        mPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                                                R.layout.popupbutton_popup,
                                                null, false);

        this.setLongClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PopupButton);

        iconRes = typedArray.getResourceId(R.styleable.PopupButton_popButtonIcon, 0);

        if (iconRes != 0)
        {
            mBinding.icon.setImageResource(iconRes);
            mPopupBinding.icImage.setImageResource(iconRes);
        }

        title = typedArray.getString(R.styleable.PopupButton_popButtonText);

        if (StringUtils.isNoneBlank(title))
        {
            mBinding.txtName.setText(title);
        }


        popupBackground = typedArray.getResourceId(R.styleable.PopupButton_popButtonBackground, 0);
        if (popupBackground != 0)
        {
            mPopupBinding.getRoot().setBackgroundResource(popupBackground);
        }

        iconBackgroud = typedArray.getResourceId(R.styleable.PopupButton_popButtonIconBackground,
                                                 0);
        if (iconBackgroud != 0)
        {
            mBinding.icon.setBackgroundResource(iconBackgroud);
        }

        initPopupWindow();
    }


    protected void initPopupWindow()
    {
        mPopupWindow = new PopupWindow(getContext());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(mPopupBinding.getRoot());
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.update();

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        mPopupBinding.getRoot().measure(width, height);

        popupheight = mPopupBinding.getRoot().getMeasuredHeight();

        popupWidth = mPopupBinding.getRoot().getMeasuredWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            mPopupWindow.showAsDropDown(mBinding.icon,
                                        this.getWidth() / 2 - popupWidth / 2,
                                        -popupheight);
        }
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (mPopupWindow != null && mPopupWindow.isShowing())
            {
                mPopupWindow.dismiss();
            }
        }
        return super.onTouchEvent(event);
    }
}
