package com.ktvdb.allen.satrok.gui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktvdb.allen.satrok.R;
import com.rey.material.drawable.RippleDrawable;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.RippleManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Allen on 15/8/26.
 */
public class StarCardView extends FrameLayout
{
    private RippleManager mRippleManager = new RippleManager();

    //    @Bind(R.id.mask)
//    ImageButton    mask;
    @Bind(R.id.name)
    TextView       txtName;
    @Bind(R.id.enName)
    TextView       txtEnName;
    @Bind(R.id.ic_image)
    ImageView      icImage;
    @Bind(R.id.rootView)
    RelativeLayout rootView;

    private String name;
    private String enName;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        txtName.setText(name);
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
        txtEnName.setText(enName);
    }

    public StarCardView(Context context)
    {
        this(context, null);
    }

    public StarCardView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public StarCardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        applyStyle(context,
                   attrs,
                   R.style.RaiseWaveButtonRippleStyle,
                   R.style.RaiseWaveButtonRippleStyle);
        inflate(context, R.layout.star_card_view, this);
        ButterKnife.bind(this);
        @SuppressLint("Recycle")
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StarCardView);

        name = a.getString(R.styleable.StarCardView_sc_name);
        enName = a.getString(R.styleable.StarCardView_sc_enName);
        int image  = a.getResourceId(R.styleable.StarCardView_sc_icon, 0);
        int pading = a.getInteger(R.styleable.StarCardView_sc_pading, 28);

        txtName.setText(name);
        txtEnName.setText(enName);
        if (image != 0)
        {
            icImage.setImageResource(image);
        }
        rootView.setPadding(pading, pading, pading, pading);
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
