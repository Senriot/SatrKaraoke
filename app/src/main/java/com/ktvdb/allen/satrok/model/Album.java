
package com.ktvdb.allen.satrok.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Album implements Serializable
{
    private String id;

    private String    albumId;
    private String    author;
    private String    title;
    private String    publishcompany;
    private String    prodcompany;
    private String    country;
    private String    language;
    private String    info;
    private String    styles;
    private Integer   styleId;
    private String    publishtime;
    private Integer   hot;
    private String    img;
    private int       status;
    private Timestamp lastUpdateTime;

    public Album()
    {
    }

    public Album(String id, String albumId, String author, String title, String img, String styles)
    {
        this.id = id;
        this.albumId = albumId;
        this.author = author;
        this.title = title;
        this.img = img;
        this.styles = styles;
    }

    public String getImg()
    {
        return img;
    }

    /**
     * @param picS1000 The pic_s1000
     */
    public void setImg(String picS1000)
    {
        this.img = picS1000;
    }

    public Timestamp getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * @return The albumId
     */
    public String getAlbumId()
    {
        return albumId;
    }

    /**
     * @param albumId The album_id
     */
    public void setAlbumId(String albumId)
    {
        this.albumId = albumId;
    }

    /**
     * @return The author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * @return The title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * @return The publishcompany
     */
    public String getPublishcompany()
    {
        return publishcompany;
    }

    /**
     * @param publishcompany The publishcompany
     */
    public void setPublishcompany(String publishcompany)
    {
        this.publishcompany = publishcompany;
    }

    /**
     * @return The prodcompany
     */
    public String getProdcompany()
    {
        return prodcompany;
    }

    /**
     * @param prodcompany The prodcompany
     */
    public void setProdcompany(String prodcompany)
    {
        this.prodcompany = prodcompany;
    }

    /**
     * @return The country
     */
    public String getCountry()
    {
        return country;
    }

    /**
     * @param country The country
     */
    public void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * @return The language
     */
    public String getLanguage()
    {
        return language;
    }

    /**
     * @param language The language
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }


    /**
     * @return The info
     */
    public String getInfo()
    {
        return info;
    }

    /**
     * @param info The info
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * @return The styles
     */
    public String getStyles()
    {
        return styles;
    }

    /**
     * @param styles The styles
     */
    public void setStyles(String styles)
    {
        this.styles = styles;
    }

    /**
     * @return The styleId
     */
    public Integer getStyleId()
    {
        return styleId;
    }

    /**
     * @param styleId The style_id
     */
    public void setStyleId(Integer styleId)
    {
        this.styleId = styleId;
    }

    /**
     * @return The publishtime
     */
    public String getPublishtime()
    {
        return publishtime;
    }

    /**
     * @param publishtime The publishtime
     */
    public void setPublishtime(String publishtime)
    {
        this.publishtime = publishtime;
    }


    /**
     * @return The hot
     */
    public Integer getHot()
    {
        return hot;
    }

    /**
     * @param hot The hot
     */
    public void setHot(Integer hot)
    {
        this.hot = hot;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    private List<Song> songs;

    public List<Song> getSongs()
    {
        return songs;
    }

    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(albumId)
                                    .append(author)
                                    .append(title)
                                    .append(publishcompany)
                                    .append(prodcompany)
                                    .append(country)
                                    .append(language)
                                    .append(info)
                                    .append(styles)
                                    .append(styleId)
                                    .append(publishtime)
                                    .append(hot)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof Album))
        {
            return false;
        }
        Album rhs = ((Album) other);
        return new EqualsBuilder().append(albumId, rhs.albumId)
                                  .append(author, rhs.author)
                                  .append(title, rhs.title)
                                  .append(publishcompany, rhs.publishcompany)
                                  .append(prodcompany, rhs.prodcompany)
                                  .append(country, rhs.country)
                                  .append(language, rhs.language)
                                  .append(info, rhs.info)
                                  .append(styles, rhs.styles)
                                  .append(styleId, rhs.styleId)
                                  .append(publishtime, rhs.publishtime)
                                  .append(hot, rhs.hot)
                                  .isEquals();
    }

}
