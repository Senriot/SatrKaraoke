package com.ktvdb.allen.satrok.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Allen on 15/7/21.
 */
public class LiveProgram extends NewokMedia implements Serializable
{
    private SysDictionary lpType;
    private int           lpWay;
    private Timestamp     liveDate;
    private Timestamp     liveStartTime;
    private Integer       duration;
    private String        description;
    private SysDictionary region;
    private String        img;
    private int           status;


    public SysDictionary getLpType()
    {
        return lpType;
    }

    public void setLpType(SysDictionary lpType)
    {
        this.lpType = lpType;
    }

    public int getLpWay()
    {
        return lpWay;
    }

    public void setLpWay(int lpWay)
    {
        this.lpWay = lpWay;
    }

    public Timestamp getLiveDate()
    {
        return liveDate;
    }

    public void setLiveDate(Timestamp liveDate)
    {
        this.liveDate = liveDate;
    }

    public Timestamp getLiveStartTime()
    {
        return liveStartTime;
    }

    public void setLiveStartTime(Timestamp liveStartTime)
    {
        this.liveStartTime = liveStartTime;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public SysDictionary getRegion()
    {
        return region;
    }

    public void setRegion(SysDictionary region)
    {
        this.region = region;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
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
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveProgram that = (LiveProgram) o;

        if (lpWay != that.lpWay) return false;
        if (status != that.status) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (liveDate != null ? !liveDate.equals(that.liveDate) : that.liveDate != null) return false;
        if (liveStartTime != null ? !liveStartTime.equals(that.liveStartTime) : that.liveStartTime != null)
            return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (img != null ? !img.equals(that.img) : that.img != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + lpWay;
        result = 31 * result + (liveDate != null ? liveDate.hashCode() : 0);
        result = 31 * result + (liveStartTime != null ? liveStartTime.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + status;
        return result;
    }
}
