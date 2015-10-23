package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.utils.BindingUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Allen on 15/9/21.
 */
public class AdPageView extends SimpleDraweeView
{

    private long interval = 5000;

    private int index = 0;

    private Handler mHandler = new Handler();

    private LinkedList<Advertisement> advertisements = new LinkedList<>();

    public long getInterval()
    {
        return interval;
    }

    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    public void addAds(Collection<Advertisement> ads)
    {
        advertisements.addAll(ads);
    }


    public AdPageView(Context context,
                      GenericDraweeHierarchy hierarchy)
    {
        super(context, hierarchy);
        init();
    }


    public AdPageView(Context context)
    {
        super(context);
        init();
    }

    public AdPageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AdPageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    void init()
    {

    }

    public void startScroll()
    {
        if (advertisements != null && !advertisements.isEmpty())
        {
            mHandler.post(runnable);
        }
    }

    public void stopScroll()
    {
        mHandler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            onScroll();
            if (advertisements.size() > 1)
            {
                mHandler.postDelayed(this, interval);
            }
        }
    };

    private void onScroll()
    {
        Advertisement advertisement = this.advertisements.removeFirst();
        BindingUtils.adImage(this, advertisement.getFileName());
        advertisements.addLast(advertisement);
    }
}
