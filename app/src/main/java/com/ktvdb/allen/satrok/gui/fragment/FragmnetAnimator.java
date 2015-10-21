package com.ktvdb.allen.satrok.gui.fragment;

import android.view.View;

import com.fragmentmaster.animator.PageAnimator;
import com.nineoldandroids.view.ViewHelper;

public class FragmnetAnimator extends PageAnimator
{

    public static final FragmnetAnimator INSTANCE = new FragmnetAnimator();

    private static final float MIN_SCALE = 0.7f;

    private static final float MIN_ALPHA = 0.0f;

    protected void transformBackgroundPage(View page, float position,
                                           boolean enter)
    {
        int pageWidth = page.getWidth();

        // Counteract the default slide transition
        ViewHelper.setTranslationX(page, pageWidth * -position);

        // Fade the page out (between MIN_ALPHA and 1)
        ViewHelper.setAlpha(page, MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position));

        // Scale the page down (between MIN_SCALE and 1)
        float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 + position);
        ViewHelper.setScaleX(page, scaleFactor);
        ViewHelper.setScaleY(page, scaleFactor);
    }

    protected void transformForegroundPage(View page, float position,
                                           boolean enter)
    {
        ViewHelper.setTranslationX(page, 0);
        ViewHelper.setAlpha(page, 1);
        ViewHelper.setScaleX(page, 1);
        ViewHelper.setScaleY(page, 1);
    }

}
