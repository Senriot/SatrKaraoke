package com.ktvdb.allen.satrok.utils;

/**
 * Created by Allen on 15/7/11.
 */
public class ViewUtils
{
    private static long lastClickTime;

    public synchronized static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isNotDoubleClick()
    {
        return !isFastDoubleClick();
    }
}
