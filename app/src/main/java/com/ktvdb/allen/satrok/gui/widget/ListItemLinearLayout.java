package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.ktvdb.allen.satrok.R;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.RippleManager;

/**
 * Created by Allen on 15/9/14.
 */
public class ListItemLinearLayout extends LinearLayout
{
    private RippleManager mRippleManager = new RippleManager();

    public ListItemLinearLayout(Context context)
    {
        this(context, null);
    }

    public ListItemLinearLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public ListItemLinearLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        applyStyle(context,
                   attrs,
                   R.style.RaiseWaveButtonRippleStyle,
                   R.style.RaiseWaveButtonRippleStyle);
    }

    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        this.mRippleManager.onCreate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBackgroundDrawable(Drawable drawable)
    {
        Drawable background = this.getBackground();
        if (background instanceof RippleDrawable && !(drawable instanceof RippleDrawable))
        {
            ((RippleDrawable) background).setBackgroundDrawable(drawable);
        }
        else
        {
            super.setBackgroundDrawable(drawable);
        }

    }

    public void setOnClickListener(OnClickListener l)
    {
        if (l == this.mRippleManager)
        {
            super.setOnClickListener(l);
        }
        else
        {
            this.mRippleManager.setOnClickListener(l);
            this.setOnClickListener(this.mRippleManager);
        }

    }

    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);
        return this.mRippleManager.onTouchEvent(event) || result;
    }
}
