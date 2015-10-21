package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/7/14.
 */
public class AlbumQueryCondition
{
    private String singerID;
    private int page = 0;
    private int size = 48;

    public String getSingerID()
    {
        return singerID;
    }

    public void setSingerID(String singerID)
    {
        this.singerID = singerID;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
}
