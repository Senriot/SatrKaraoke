
package com.ktvdb.allen.satrok.socket;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PlayAction
{

    @SerializedName("SKID")
    private String SKID = "05";
    @SerializedName("PlayID")
    private String PlayID;
    @SerializedName("ProgramType")
    private String ProgramType;
    @SerializedName("ProgramCode")
    private String ProgramCode;
    @SerializedName("DeviceCode")
    private String DeviceCode  = "STB201401-000001";
    @SerializedName("DeviceClass")
    private String DeviceClass = "01";
    @SerializedName("PTime")
    private String PTime;
    @SerializedName("PType")
    private String PType;

    /**
     * @return The SKID
     */
    public String getSKID()
    {
        return SKID;
    }

    /**
     * @param SKID The SKID
     */
    public void setSKID(String SKID)
    {
        this.SKID = SKID;
    }

    /**
     * @return The PlayID
     */
    public String getPlayID()
    {
        return PlayID;
    }

    /**
     * @param PlayID The PlayID
     */
    public void setPlayID(String PlayID)
    {
        this.PlayID = PlayID;
    }

    /**
     * @return The ProgramType
     */
    public String getProgramType()
    {
        return ProgramType;
    }

    /**
     * @param ProgramType The ProgramType
     */
    public void setProgramType(String ProgramType)
    {
        this.ProgramType = ProgramType;
    }

    /**
     * @return The ProgramCode
     */
    public String getProgramCode()
    {
        return ProgramCode;
    }

    /**
     * @param ProgramCode The ProgramCode
     */
    public void setProgramCode(String ProgramCode)
    {
        this.ProgramCode = ProgramCode;
    }

    /**
     * @return The DeviceCode
     */
    public String getDeviceCode()
    {
        return DeviceCode;
    }

    /**
     * @param DeviceCode The DeviceCode
     */
    public void setDeviceCode(String DeviceCode)
    {
        this.DeviceCode = DeviceCode;
    }

    /**
     * @return The DeviceClass
     */
    public String getDeviceClass()
    {
        return DeviceClass;
    }

    /**
     * @param DeviceClass The DeviceClass
     */
    public void setDeviceClass(String DeviceClass)
    {
        this.DeviceClass = DeviceClass;
    }

    /**
     * @return The PTime
     */
    public String getPTime()
    {
        return PTime;
    }

    /**
     * @param PTime The PTime
     */
    public void setPTime(String PTime)
    {
        this.PTime = PTime;
    }

    /**
     * @return The PType
     */
    public String getPType()
    {
        return PType;
    }

    /**
     * @param PType The PType
     */
    public void setPType(String PType)
    {
        this.PType = PType;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(SKID)
                                    .append(PlayID)
                                    .append(ProgramType)
                                    .append(ProgramCode)
                                    .append(DeviceCode)
                                    .append(DeviceClass)
                                    .append(PTime)
                                    .append(PType)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof PlayAction))
        {
            return false;
        }
        PlayAction rhs = ((PlayAction) other);
        return new EqualsBuilder().append(SKID, rhs.SKID).append(PlayID, rhs.PlayID).append(
                ProgramType,
                rhs.ProgramType).append(ProgramCode, rhs.ProgramCode).append(DeviceCode,
                                                                             rhs.DeviceCode).append(
                DeviceClass,
                rhs.DeviceClass).append(PTime, rhs.PTime).append(PType, rhs.PType).isEquals();
    }

}
