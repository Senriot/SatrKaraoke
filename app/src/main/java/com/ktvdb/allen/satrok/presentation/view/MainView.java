package com.ktvdb.allen.satrok.presentation.view;

import android.graphics.Bitmap;

import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.FullSearchResult;

import java.util.List;

/**
 * Created by Allen on 15/8/27.
 */
public interface MainView
{
    void setTextAds(List<Advertisement> advertisements);

    void showBitmap(Bitmap bitmap);

    void setMute(boolean b);

    void showSearchView(FullSearchResult fullSearchResult);
}
