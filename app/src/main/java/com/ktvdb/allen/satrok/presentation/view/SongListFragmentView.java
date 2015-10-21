package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

/**
 * Created by Allen on 15/8/30.
 */
public interface SongListFragmentView
{
    void updateTabView(List<SysDictionary> tabs);

    void onLoadcompled(PageResponse page);

    void setImageAds(List<Advertisement> advertisements);
}
