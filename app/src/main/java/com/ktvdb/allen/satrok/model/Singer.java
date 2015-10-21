package com.ktvdb.allen.satrok.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Allen on 15/7/12.
 */
public class Singer implements Serializable
{
    private String id;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    private String code;

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    private String simpName;

    public String getSimpName()
    {
        return simpName;
    }

    public void setSimpName(String simpName)
    {
        this.simpName = simpName;
    }

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

    private String jpnName;

    public String getJpnName()
    {
        return jpnName;
    }

    public void setJpnName(String jpnName)
    {
        this.jpnName = jpnName;
    }

    private String korName;

    public String getKorName()
    {
        return korName;
    }

    public void setKorName(String korName)
    {
        this.korName = korName;
    }

    private String oldName;

    public String getOldName()
    {
        return oldName;
    }

    public void setOldName(String oldName)
    {
        this.oldName = oldName;
    }

    private String namesimplicity;

    public String getNamesimplicity()
    {
        return namesimplicity;
    }

    public void setNamesimplicity(String namesimplicity)
    {
        this.namesimplicity = namesimplicity;
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

    private int sex;

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    private String birthday;

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    private String birthPlace;

    public String getBirthPlace()
    {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace)
    {
        this.birthPlace = birthPlace;
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

    private String constellation;

    public String getConstellation()
    {
        return constellation;
    }

    public void setConstellation(String constellation)
    {
        this.constellation = constellation;
    }


    private String familyMembers;

    public String getFamilyMembers()
    {
        return familyMembers;
    }

    public void setFamilyMembers(String familyMembers)
    {
        this.familyMembers = familyMembers;
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

    private String nature;

    public String getNature()
    {
        return nature;
    }

    public void setNature(String nature)
    {
        this.nature = nature;
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

    private int status;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    private Timestamp lastUpdateTime;

    public Timestamp getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }


    private String artistId;

    public String getArtistId()
    {
        return artistId;
    }

    public void setArtistId(String artistId)
    {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Singer singer = (Singer) o;

        if (sex != singer.sex) return false;
        if (status != singer.status) return false;
        if (id != null ? !id.equals(singer.id) : singer.id != null) return false;
        if (code != null ? !code.equals(singer.code) : singer.code != null) return false;
        if (simpName != null ? !simpName.equals(singer.simpName) : singer.simpName != null)
            return false;
        if (tradName != null ? !tradName.equals(singer.tradName) : singer.tradName != null)
            return false;
        if (enName != null ? !enName.equals(singer.enName) : singer.enName != null) return false;
        if (jpnName != null ? !jpnName.equals(singer.jpnName) : singer.jpnName != null)
            return false;
        if (korName != null ? !korName.equals(singer.korName) : singer.korName != null)
            return false;
        if (oldName != null ? !oldName.equals(singer.oldName) : singer.oldName != null)
            return false;
        if (namesimplicity != null ? !namesimplicity.equals(singer.namesimplicity)
                                   : singer.namesimplicity != null)
            return false;
        if (wordCount != null ? !wordCount.equals(singer.wordCount) : singer.wordCount != null)
            return false;
        if (stroke != null ? !stroke.equals(singer.stroke) : singer.stroke != null) return false;
        if (birthday != null ? !birthday.equals(singer.birthday) : singer.birthday != null)
            return false;
        if (birthPlace != null ? !birthPlace.equals(singer.birthPlace) : singer.birthPlace != null)
            return false;
        if (img != null ? !img.equals(singer.img) : singer.img != null) return false;
        if (constellation != null ? !constellation.equals(singer.constellation)
                                  : singer.constellation != null)
            return false;
        if (familyMembers != null ? !familyMembers.equals(singer.familyMembers)
                                  : singer.familyMembers != null)
            return false;
        if (language != null ? !language.equals(singer.language) : singer.language != null)
            return false;
        if (nature != null ? !nature.equals(singer.nature) : singer.nature != null) return false;
        if (description != null ? !description.equals(singer.description)
                                : singer.description != null) return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(singer.lastUpdateTime)
                                   : singer.lastUpdateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (simpName != null ? simpName.hashCode() : 0);
        result = 31 * result + (tradName != null ? tradName.hashCode() : 0);
        result = 31 * result + (enName != null ? enName.hashCode() : 0);
        result = 31 * result + (jpnName != null ? jpnName.hashCode() : 0);
        result = 31 * result + (korName != null ? korName.hashCode() : 0);
        result = 31 * result + (oldName != null ? oldName.hashCode() : 0);
        result = 31 * result + (namesimplicity != null ? namesimplicity.hashCode() : 0);
        result = 31 * result + (wordCount != null ? wordCount.hashCode() : 0);
        result = 31 * result + (stroke != null ? stroke.hashCode() : 0);
        result = 31 * result + sex;
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (birthPlace != null ? birthPlace.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + (constellation != null ? constellation.hashCode() : 0);
        result = 31 * result + (familyMembers != null ? familyMembers.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (nature != null ? nature.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }
}
