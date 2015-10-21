package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.gui.adapters.LiveAdapter;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

/**
 * Created by Allen on 15/8/28.
 */
public interface LiveListView
{
    void setImageAds(List<Advertisement> advertisements);
    void updateTab(List<SysDictionary> sysDictionaries);

    void initList(LiveAdapter mAdapter);
}
