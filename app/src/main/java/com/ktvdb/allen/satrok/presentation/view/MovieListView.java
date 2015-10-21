package com.ktvdb.allen.satrok.presentation.view;

import com.ktvdb.allen.satrok.gui.adapters.MovieAdapter;
import com.ktvdb.allen.satrok.model.SysDictionary;

import java.util.List;

/**
 * Created by Allen on 15/8/28.
 */
public interface MovieListView
{
    void updateTabView(List<SysDictionary> sysDictionaries);

    void initListView(MovieAdapter mAdapter);
}
