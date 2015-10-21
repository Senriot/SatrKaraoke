package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Album;

import java.util.List;

/**
 * Created by Allen on 15/8/27.
 */
public interface MainPageView
{
    void onPlayRecommendAlbums(List<Album> albums);

    void setImageAds(List<Advertisement> advertisements);

}
