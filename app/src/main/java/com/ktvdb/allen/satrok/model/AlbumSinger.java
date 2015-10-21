package com.ktvdb.allen.satrok.model;

import java.sql.Timestamp;

/**
 * Created by Allen on 15/7/20.
 */
public class AlbumSinger
{
    private String id;

    private Singer singer;
    private Album  album;

    private Timestamp lastUpdateTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Singer getSinger()
    {
        return singer;
    }

    public void setSinger(Singer singer)
    {
        this.singer = singer;
    }

    public Album getAlbum()
    {
        return album;
    }

    public void setAlbum(Album album)
    {
        this.album = album;
    }


    public Timestamp getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumSinger that = (AlbumSinger) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime)
                                   : that.lastUpdateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }
}
