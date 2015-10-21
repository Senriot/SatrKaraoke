package com.ktvdb.allen.satrok.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Allen on 15/7/21.
 */
public class Movie extends NewokMedia implements Serializable
{
    private String tradName;

    public String getTradName()
    {
        return tradName;
    }

    public void setTradName(String tradName)
    {
        this.tradName = tradName;
    }

    private String enName;

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    private Integer wordCount;

    public Integer getWordCount()
    {
        return wordCount;
    }

    public void setWordCount(Integer wordCount)
    {
        this.wordCount = wordCount;
    }

    private String stroke;

    public String getStroke()
    {
        return stroke;
    }

    public void setStroke(String stroke)
    {
        this.stroke = stroke;
    }

    private String anotherName;

    public String getAnotherName()
    {
        return anotherName;
    }

    public void setAnotherName(String anotherName)
    {
        this.anotherName = anotherName;
    }

    private String director;

    public String getDirector()
    {
        return director;
    }

    public void setDirector(String director)
    {
        this.director = director;
    }

    private String starring;

    public String getStarring()
    {
        return starring;
    }

    public void setStarring(String starring)
    {
        this.starring = starring;
    }

    private String productionFactory;

    public String getProductionFactory()
    {
        return productionFactory;
    }

    public void setProductionFactory(String productionFactory)
    {
        this.productionFactory = productionFactory;
    }

    private String description;

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private Integer duration;

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    private String language;

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    private String region;

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    private String img;

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }


    private String movieFileName;

    public String getMovieFileName()
    {
        return movieFileName;
    }

    public void setMovieFileName(String movieFileName)
    {
        this.movieFileName = movieFileName;
    }

    private String movieFilePath;

    public String getMovieFilePath()
    {
        return movieFilePath;
    }

    public void setMovieFilePath(String movieFilePath)
    {
        this.movieFilePath = movieFilePath;
    }


    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    private List<MovieMovieType> movieTypes;

    public List<MovieMovieType> getMovieTypes()
    {
        return movieTypes;
    }

    public void setMovieTypes(List<MovieMovieType> movieTypes)
    {
        this.movieTypes = movieTypes;
    }




    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (status != movie.status) return false;
        if (id != null ? !id.equals(movie.id) : movie.id != null) return false;
        if (tradName != null ? !tradName.equals(movie.tradName) : movie.tradName != null) return false;
        if (enName != null ? !enName.equals(movie.enName) : movie.enName != null) return false;
        if (wordCount != null ? !wordCount.equals(movie.wordCount) : movie.wordCount != null) return false;
        if (stroke != null ? !stroke.equals(movie.stroke) : movie.stroke != null) return false;
        if (anotherName != null ? !anotherName.equals(movie.anotherName) : movie.anotherName != null) return false;
        if (director != null ? !director.equals(movie.director) : movie.director != null) return false;
        if (starring != null ? !starring.equals(movie.starring) : movie.starring != null) return false;
        if (productionFactory != null ? !productionFactory.equals(movie.productionFactory) : movie.productionFactory != null)
            return false;
        if (description != null ? !description.equals(movie.description) : movie.description != null) return false;
        if (duration != null ? !duration.equals(movie.duration) : movie.duration != null) return false;
        if (language != null ? !language.equals(movie.language) : movie.language != null) return false;
        if (region != null ? !region.equals(movie.region) : movie.region != null) return false;
        if (img != null ? !img.equals(movie.img) : movie.img != null) return false;
        if (movieFileName != null ? !movieFileName.equals(movie.movieFileName) : movie.movieFileName != null)
            return false;
        if (movieFilePath != null ? !movieFilePath.equals(movie.movieFilePath) : movie.movieFilePath != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tradName != null ? tradName.hashCode() : 0);
        result = 31 * result + (enName != null ? enName.hashCode() : 0);
        result = 31 * result + (wordCount != null ? wordCount.hashCode() : 0);
        result = 31 * result + (stroke != null ? stroke.hashCode() : 0);
        result = 31 * result + (anotherName != null ? anotherName.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (starring != null ? starring.hashCode() : 0);
        result = 31 * result + (productionFactory != null ? productionFactory.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (movieFileName != null ? movieFileName.hashCode() : 0);
        result = 31 * result + (movieFilePath != null ? movieFilePath.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }
}
