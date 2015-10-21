
package com.ktvdb.allen.satrok.socket;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


public class RoomStatusResponses
{

    @SerializedName("SKID")
    private String SKID = "03";
    @SerializedName("MsgID")
    private String MsgID;
    @SerializedName("Succeed")
    private String              Succeed              = "1";
    @SerializedName("DeviceCode")
    private String              DeviceCode           = "STB201401-000001";
    @SerializedName("DeviceClass")
    private String              DeviceClass          = "01";
    @SerializedName("Msg")
    private String              Msg                  = "ok";
    @SerializedName("Ext")
    private List<RoomStatusExt> Ext                  = new ArrayList<RoomStatusExt>();

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

    /**
     * @return The Succeed
     */
    public String getSucceed()
    {
        return Succeed;
    }

    /**
     * @param Succeed The Succeed
     */
    public void setSucceed(String Succeed)
    {
        this.Succeed = Succeed;
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
     * @return The Msg
     */
    public String getMsg()
    {
        return Msg;
    }

    /**
     * @param Msg The Msg
     */
    public void setMsg(String Msg)
    {
        this.Msg = Msg;
    }

    /**
     * @return The Ext
     */
    public List<RoomStatusExt> getExt()
    {
        return Ext;
    }

    /**
     * @param Ext The Ext
     */
    public void setExt(List<RoomStatusExt> Ext)
    {
        this.Ext = Ext;
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
                                    .append(MsgID)
                                    .append(Succeed)
                                    .append(DeviceCode)
                                    .append(DeviceClass)
                                    .append(Msg)
                                    .append(Ext)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof RoomStatusResponses))
        {
            return false;
        }
        RoomStatusResponses rhs = ((RoomStatusResponses) other);
        return new EqualsBuilder().append(SKID, rhs.SKID)
                                  .append(MsgID, rhs.MsgID)
                                  .append(Succeed,
                                          rhs.Succeed)
                                  .append(DeviceCode, rhs.DeviceCode)
                                  .append(DeviceClass, rhs.DeviceClass)
                                  .append(Msg, rhs.Msg)
                                  .append(Ext, rhs.Ext)
                                  .isEquals();
    }

}
