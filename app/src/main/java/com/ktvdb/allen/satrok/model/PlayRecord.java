package com.ktvdb.allen.satrok.model;

import com.ktvdb.allen.satrok.StarokApplication;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Allen on 15/9/30.
 */
public class PlayRecord
{

    public static class RecordBuilder
    {
        PlayRecord record;

        public RecordBuilder()
        {
            record = new PlayRecord();
            record.id = UUID.randomUUID().toString();
            record.ktvNetRoomCode = StarokApplication.getAppContext()
                                                     .getComponent()
                                                     .configManager()
                                                     .getRoomInfo()
                                                     .getCode();
            record.ktvNetCode = "001";
            record.stbCode = StarokApplication.getAppContext().uuid();
            record.pTime = new Timestamp(System.currentTimeMillis());
        }

        public RecordBuilder playId(String id)
        {
            record.playId = id;
            return this;
        }

        public RecordBuilder action(String code)
        {
            record.actionCode = code;
            return this;
        }

        public RecordBuilder mediaCode(String code)
        {
            record.programCode = code;
            return this;
        }

        public RecordBuilder type(String type)
        {
            record.pType = type;
            return this;
        }

        public PlayRecord builder()
        {
            return record;
        }
    }

    private String    id;
    private String    actionCode;
    private String    playId;
    private String    programCode;
    private String    ktvNetCode;
    private String    ktvNetRoomCode;
    private String    stbCode;
    private Timestamp pTime;
    private String    pType;
    private Timestamp creationTime;
    private int lastUpToMiniPcStatus = 0;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getActionCode()
    {
        return actionCode;
    }

    public void setActionCode(String actionCode)
    {
        this.actionCode = actionCode;
    }

    public String getPlayId()
    {
        return playId;
    }

    public void setPlayId(String playId)
    {
        this.playId = playId;
    }

    public String getProgramCode()
    {
        return programCode;
    }

    public void setProgramCode(String programCode)
    {
        this.programCode = programCode;
    }

    public String getKtvNetCode()
    {
        return ktvNetCode;
    }

    public void setKtvNetCode(String ktvNetCode)
    {
        this.ktvNetCode = ktvNetCode;
    }

    public String getKtvNetRoomCode()
    {
        return ktvNetRoomCode;
    }

    public void setKtvNetRoomCode(String ktvNetRoomCode)
    {
        this.ktvNetRoomCode = ktvNetRoomCode;
    }

    public String getStbCode()
    {
        return stbCode;
    }

    public void setStbCode(String stbCode)
    {
        this.stbCode = stbCode;
    }

    public Timestamp getpTime()
    {
        return pTime;
    }

    public void setpTime(Timestamp pTime)
    {
        this.pTime = pTime;
    }

    public String getpType()
    {
        return pType;
    }

    public void setpType(String pType)
    {
        this.pType = pType;
    }

    public Timestamp getCreationTime()
    {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime)
    {
        this.creationTime = creationTime;
    }

    public int getLastUpToMiniPcStatus()
    {
        return lastUpToMiniPcStatus;
    }

    public void setLastUpToMiniPcStatus(int lastUpToMiniPcStatus)
    {
        this.lastUpToMiniPcStatus = lastUpToMiniPcStatus;
    }
}
