package com.ktvdb.allen.satrok.model;

import java.sql.Timestamp;

/**
 * Created by Allen on 15/7/21.
 */
public class MovieMovieType
{
    private String        id;
    private Movie         movie;
    private SysDictionary movieType;
    private Timestamp     lastUpdateTime;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Movie getMovie()
    {
        return movie;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    public SysDictionary getMovieType()
    {
        return movieType;
    }

    public void setMovieType(SysDictionary movieType)
    {
        this.movieType = movieType;
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

        MovieMovieType that = (MovieMovieType) o;

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
