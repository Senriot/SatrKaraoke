package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.widget.ImageButton;

/**
 * Created by Allen on 15/7/14.
 */
public class SquareButton extends ImageButton
{
    public SquareButton(Context context)
    {
        super(context);
    }

    public SquareButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0,
                                                                                 heightMeasureSpec)); // Children are just made to fill our space.
        int childWidthSize  = getMeasuredHeight();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
                                                                           MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
