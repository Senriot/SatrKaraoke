package com.ktvdb.allen.satrok.model;


import java.sql.Timestamp;

/**
 * Created by Allen on 15/8/19.
 */
public class Advertisement
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

    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    private String positionCode;

    public String getPositionCode()
    {
        return positionCode;
    }

    public void setPositionCode(String positionCode)
    {
        this.positionCode = positionCode;
    }

    private String type;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    private String fileName;

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    private String filePath;

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    private String content;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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

    private String specification;

    public String getSpecification()
    {
        return specification;
    }

    public void setSpecification(String specification)
    {
        this.specification = specification;
    }

    private Integer isTest;

    public Integer getIsTest()
    {
        return isTest;
    }

    public void setIsTest(Integer isTest)
    {
        this.isTest = isTest;
    }

    private Integer testTime;

    public Integer getTestTime()
    {
        return testTime;
    }

    public void setTestTime(Integer testTime)
    {
        this.testTime = testTime;
    }

    private Timestamp putOnStartDate;

    public Timestamp getPutOnStartDate()
    {
        return putOnStartDate;
    }

    public void setPutOnStartDate(Timestamp putOnStartDate)
    {
        this.putOnStartDate = putOnStartDate;
    }

    private Timestamp putOnEndDate;

    public Timestamp getPutOnEndDate()
    {
        return putOnEndDate;
    }

    public void setPutOnEndDate(Timestamp putOnEndDate)
    {
        this.putOnEndDate = putOnEndDate;
    }

    private Timestamp playStartTime;

    public Timestamp getPlayStartTime()
    {
        return playStartTime;
    }

    public void setPlayStartTime(Timestamp playStartTime)
    {
        this.playStartTime = playStartTime;
    }

    private Timestamp playEndTime;

    public Timestamp getPlayEndTime()
    {
        return playEndTime;
    }

    public void setPlayEndTime(Timestamp playEndTime)
    {
        this.playEndTime = playEndTime;
    }

    private String roomCode;

    public String getRoomCode()
    {
        return roomCode;
    }

    public void setRoomCode(String roomCode)
    {
        this.roomCode = roomCode;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Advertisement that = (Advertisement) o;

        if (status != that.status)
        {
            return false;
        }
        if (id != null ? !id.equals(that.id) : that.id != null)
        {
            return false;
        }
        if (code != null ? !code.equals(that.code) : that.code != null)
        {
            return false;
        }
        if (name != null ? !name.equals(that.name) : that.name != null)
        {
            return false;
        }
        if (positionCode != null ? !positionCode.equals(that.positionCode) : that.positionCode != null)
        {
            return false;
        }
        if (type != null ? !type.equals(that.type) : that.type != null)
        {
            return false;
        }
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null)
        {
            return false;
        }
        if (filePath != null ? !filePath.equals(that.filePath) : that.filePath != null)
        {
            return false;
        }
        if (content != null ? !content.equals(that.content) : that.content != null)
        {
            return false;
        }
        if (duration != null ? !duration.equals(that.duration) : that.duration != null)
        {
            return false;
        }
        if (specification != null ? !specification.equals(that.specification) : that.specification != null)
        {
            return false;
        }
        if (isTest != null ? !isTest.equals(that.isTest) : that.isTest != null)
        {
            return false;
        }
        if (testTime != null ? !testTime.equals(that.testTime) : that.testTime != null)
        {
            return false;
        }
        if (putOnStartDate != null ? !putOnStartDate.equals(that.putOnStartDate) : that.putOnStartDate != null)
        {
            return false;
        }
        if (putOnEndDate != null ? !putOnEndDate.equals(that.putOnEndDate) : that.putOnEndDate != null)
        {
            return false;
        }
        if (playStartTime != null ? !playStartTime.equals(that.playStartTime) : that.playStartTime != null)
        {
            return false;
        }
        if (playEndTime != null ? !playEndTime.equals(that.playEndTime) : that.playEndTime != null)
        {
            return false;
        }
        if (roomCode != null ? !roomCode.equals(that.roomCode) : that.roomCode != null)
        {
            return false;
        }
        if (lastUpdateTime != null ? !lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (positionCode != null ? positionCode.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (specification != null ? specification.hashCode() : 0);
        result = 31 * result + (isTest != null ? isTest.hashCode() : 0);
        result = 31 * result + (testTime != null ? testTime.hashCode() : 0);
        result = 31 * result + (putOnStartDate != null ? putOnStartDate.hashCode() : 0);
        result = 31 * result + (putOnEndDate != null ? putOnEndDate.hashCode() : 0);
        result = 31 * result + (playStartTime != null ? playStartTime.hashCode() : 0);
        result = 31 * result + (playEndTime != null ? playEndTime.hashCode() : 0);
        result = 31 * result + (roomCode != null ? roomCode.hashCode() : 0);
        result = 31 * result + status;
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }
}
