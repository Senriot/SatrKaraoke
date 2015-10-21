
package com.ktvdb.allen.satrok.socket;


import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RoomStatusRequest
{

    @SerializedName("Broadcast")
    private String Broadcast;
    @SerializedName("SKID")
    private String SKID;
    @SerializedName("DeviceCode")
    private String DeviceCode;
    @SerializedName("DeviceClass")
    private String DeviceClass;
    @SerializedName("MsgID")
    private String MsgID;

    /**
     * @return The Broadcast
     */
    public String getBroadcast()
    {
        return Broadcast;
    }

    /**
     * @param Broadcast The Broadcast
     */
    public void setBroadcast(String Broadcast)
    {
        this.Broadcast = Broadcast;
    }

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
     * @return The MsgID
     */
    public String getMsgID()
    {
        return MsgID;
    }

    /**
     * @param MsgID The MsgID
     */
    public void setMsgID(String MsgID)
    {
        this.MsgID = MsgID;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(Broadcast).append(SKID).append(DeviceCode).append(
                DeviceClass).append(MsgID).toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof RoomStatusRequest))
        {
            return false;
        }
        RoomStatusRequest rhs = ((RoomStatusRequest) other);
        return new EqualsBuilder().append(Broadcast, rhs.Broadcast)
                                  .append(SKID, rhs.SKID)
                                  .append(DeviceCode,
                                          rhs.DeviceCode)
                                  .append(DeviceClass, rhs.DeviceClass)
                                  .append(MsgID, rhs.MsgID)
                                  .isEquals();
    }

}
