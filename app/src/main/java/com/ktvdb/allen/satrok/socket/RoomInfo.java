
package com.ktvdb.allen.satrok.socket;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class RoomInfo
{

    @SerializedName("Broadcast")
    private String Broadcast;
    @SerializedName("SKID")
    private String SKID;
    @SerializedName("DeviceCode")
    private String DeviceCode;
    @SerializedName("DeviceClass")
    private String DeviceClass;
    @SerializedName("KTVNetCode")
    private String KTVNetCode;
    @SerializedName("KTVNetName")
    private String KTVNetName;
    @SerializedName("RoomName")
    private String RoomName;
    @SerializedName("RoomCode")
    private String RoomCode;
    @SerializedName("Random")
    private String Random;
    @SerializedName("AuthCheckCode")
    private String AuthCheckCode;

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
     * @return The KTVNetCode
     */
    public String getKTVNetCode()
    {
        return KTVNetCode;
    }

    /**
     * @param KTVNetCode The KTVNetCode
     */
    public void setKTVNetCode(String KTVNetCode)
    {
        this.KTVNetCode = KTVNetCode;
    }

    /**
     * @return The KTVNetName
     */
    public String getKTVNetName()
    {
        return KTVNetName;
    }

    /**
     * @param KTVNetName The KTVNetName
     */
    public void setKTVNetName(String KTVNetName)
    {
        this.KTVNetName = KTVNetName;
    }

    /**
     * @return The RoomName
     */
    public String getRoomName()
    {
        return RoomName;
    }

    /**
     * @param RoomName The RoomName
     */
    public void setRoomName(String RoomName)
    {
        this.RoomName = RoomName;
    }

    /**
     * @return The RoomCode
     */
    public String getRoomCode()
    {
        return RoomCode;
    }

    /**
     * @param RoomCode The RoomCode
     */
    public void setRoomCode(String RoomCode)
    {
        this.RoomCode = RoomCode;
    }

    /**
     * @return The Random
     */
    public String getRandom()
    {
        return Random;
    }

    /**
     * @param Random The Random
     */
    public void setRandom(String Random)
    {
        this.Random = Random;
    }

    /**
     * @return The AuthCheckCode
     */
    public String getAuthCheckCode()
    {
        return AuthCheckCode;
    }

    /**
     * @param AuthCheckCode The AuthCheckCode
     */
    public void setAuthCheckCode(String AuthCheckCode)
    {
        this.AuthCheckCode = AuthCheckCode;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(Broadcast)
                                    .append(SKID)
                                    .append(DeviceCode)
                                    .append(DeviceClass)
                                    .append(KTVNetCode)
                                    .append(KTVNetName)
                                    .append(RoomName)
                                    .append(RoomCode)
                                    .append(Random)
                                    .append(AuthCheckCode)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof RoomInfo))
        {
            return false;
        }
        RoomInfo rhs = ((RoomInfo) other);
        return new EqualsBuilder().append(Broadcast, rhs.Broadcast)
                                  .append(SKID, rhs.SKID)
                                  .append(DeviceCode,
                                          rhs.DeviceCode)
                                  .append(DeviceClass, rhs.DeviceClass)
                                  .append(KTVNetCode,
                                          rhs.KTVNetCode)
                                  .append(KTVNetName,
                                          rhs.KTVNetName)
                                  .append(RoomName, rhs.RoomName)
                                  .append(RoomCode, rhs.RoomCode)
                                  .append(Random, rhs.Random)
                                  .append(AuthCheckCode, rhs.AuthCheckCode)
                                  .isEquals();
    }

}
