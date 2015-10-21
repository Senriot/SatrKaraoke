package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.gui.adapters.SingerListAdapter;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

/**
 * Created by Allen on 15/9/2.
 */
public interface SingerListView extends OnItemClickListener
{
    void updateTabView(List<SysDictionary> sysDictionaries);

    void setAdapter(SingerListAdapter mAdapter);

    void refresh(boolean hsMore);

    void setImageAds(List<Advertisement> advertisements);
}
