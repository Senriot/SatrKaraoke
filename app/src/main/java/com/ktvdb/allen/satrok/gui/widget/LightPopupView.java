package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import com.ktvdb.allen.satrok.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * TODO: document your custom view class.
 */
public class LightPopupView extends FrameLayout
{

    @Bind(R.id.b_wenxin)
    ImageButton  mBWenxin;
    @Bind(R.id.b_mingliang)
    ImageButton  mBMingliang;
    @Bind(R.id.b_donggan)
    ImageButton  mBDonggan;
    @Bind(R.id.b_close)
    ImageButton  mBClose;
    @Bind(R.id.buttons)
    LinearLayout mButtons;
    @Bind(R.id.b_miss)
    ImageButton  mBMiss;

    private PopupWindow mPopupWindow;

    public LightPopupView(Context context)
    {
        this(context, null);
    }

    public LightPopupView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LightPopupView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.light_popup_view, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.b_wenxin, R.id.b_mingliang, R.id.b_donggan, R.id.b_close, R.id.b_miss})
    void btnCilck()
    {
        if (mPopupWindow != null && mPopupWindow.isShowing())
        {
            mPopupWindow.dismiss();
        }
    }


    public static void show(Context context, View view)
    {
        PopupWindow mPopupWindow = new PopupWindow(context);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        mPopupWindow.showAsDropDown(view);
    }
}
