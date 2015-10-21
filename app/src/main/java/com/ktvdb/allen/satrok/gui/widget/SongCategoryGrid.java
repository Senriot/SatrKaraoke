package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.SongCatagoryGridBinding;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.RippleManager;

/**
 * Created by Allen on 15/10/16.
 */
public class SongCategoryGrid extends FrameLayout
{
    public String cnName;
    public String enName;
    public int    iconId;
    public String context;

    private RippleManager mRippleManager = new RippleManager();

    SongCatagoryGridBinding binding;

    public SongCategoryGrid(Context context)
    {
        this(context, null);
    }

    public SongCategoryGrid(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SongCategoryGrid(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                          R.layout.song_catagory_grid,
                                          this,
                                          true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SongCategoryGrid);
        cnName = typedArray.getString(R.styleable.SongCategoryGrid_scg_cnName);
        binding.cnName.setText(cnName);
        binding.enName.setText(typedArray.getString(R.styleable.SongCategoryGrid_scg_enName));
        binding.icon.setImageResource(typedArray.getResourceId(R.styleable.SongCategoryGrid_scg_iconResID,
                                                               0));
        binding.content.setText(typedArray.getString(R.styleable.SongCategoryGrid_scg_content));
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


    public void setContext(String context)
    {
        this.context = context;
        binding.content.setText(context);
    }
}
