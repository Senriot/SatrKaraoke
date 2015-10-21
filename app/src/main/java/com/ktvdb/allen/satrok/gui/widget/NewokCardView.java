package com.ktvdb.allen.satrok.gui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ktvdb.allen.satrok.R;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.RippleManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen on 15/7/11.
 */
public class NewokCardView extends FrameLayout
{
    private RippleManager mRippleManager = new RippleManager();


    private String title;

    public NewokCardView(Context context)
    {
        this(context, null);
    }

    public NewokCardView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public NewokCardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        applyStyle(context,
                   attrs,
                   R.style.RaiseWaveButtonRippleStyle,
                   R.style.RaiseWaveButtonRippleStyle);
        ButterKnife.bind(this);
        @SuppressLint("Recycle")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewokCardView);
        int imageId = typedArray.getResourceId(R.styleable.NewokCardView_cardImage, 0);
        title = typedArray.getString(R.styleable.NewokCardView_cardTitle);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageId);
        addView(imageView);
    }


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
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
