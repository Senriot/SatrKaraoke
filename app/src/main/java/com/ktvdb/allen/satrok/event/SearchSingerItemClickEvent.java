package com.ktvdb.allen.satrok.event;

import com.ktvdb.allen.satrok.model.Singer;

/**
 * Created by Allen on 15/9/14.
 */
public class SearchSingerItemClickEvent
{
    public final Singer singer;

    public SearchSingerItemClickEvent(Singer singer) {this.singer = singer;}
}
