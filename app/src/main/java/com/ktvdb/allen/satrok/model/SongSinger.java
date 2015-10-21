package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/7/21.
 */
public class SongSinger
{
    private String id;
    private Song   song;
    private Singer singer;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Song getSong()
    {
        return song;
    }

    public void setSong(Song song)
    {
        this.song = song;
    }

    public Singer getSinger()
    {
        return singer;
    }

    public void setSinger(Singer singer)
    {
        this.singer = singer;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SongSinger that = (SongSinger) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }
}
