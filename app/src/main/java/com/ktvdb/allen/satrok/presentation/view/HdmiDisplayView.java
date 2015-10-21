package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.model.Advertisement;

import java.util.List;

/**
 * Created by Allen on 15/8/29.
 */
public interface HdmiDisplayView
{

    void setTextAds(List<Advertisement> advertisements);

    void setImageAds(List<Advertisement> t, List<Advertisement> b);
}
