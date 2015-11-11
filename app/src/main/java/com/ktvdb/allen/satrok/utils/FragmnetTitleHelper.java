package com.ktvdb.allen.satrok.utils;

import android.content.Context;

import com.fragmentmaster.app.IMasterFragment;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;

/**
 * Created by Allen on 15/10/24.
 */
public class FragmnetTitleHelper
{
    public static String getFragmnetTitle(Context context, IMasterFragment fragment)
    {
        String fragmnetTitle = "";
        Class  clazz         = fragment.getClass();
        if (clazz.isAnnotationPresent(FragmnetTitle.class))
        {
            FragmnetTitle outValue = (FragmnetTitle) clazz.getAnnotation(FragmnetTitle.class);
            fragmnetTitle = outValue.value();
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(fragmnetTitle))
        {
            fragmnetTitle = fragment.getRequest().getStringExtra("title");
        }
        return fragmnetTitle;
    }
}
