
package com.ktvdb.allen.satrok.socket;


import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class RoomStatusExt
{

    @SerializedName("ProgramType")
    private String ProgramType;
    @SerializedName("Status")
    private String Status;
    @SerializedName("ProgramCode")
    private String ProgramCode;
    @SerializedName("PlayedTime")
    private String PlayedTime;
    @SerializedName("Filename")
    private String Filename;

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
     * @return The Status
     */
    public String getStatus()
    {
        return Status;
    }

    /**
     * @param Status The Status
     */
    public void setStatus(String Status)
    {
        this.Status = Status;
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
     * @return The PlayedTime
     */
    public String getPlayedTime()
    {
        return PlayedTime;
    }

    /**
     * @param PlayedTime The PlayedTime
     */
    public void setPlayedTime(String PlayedTime)
    {
        this.PlayedTime = PlayedTime;
    }

    /**
     * @return The Filename
     */
    public String getFilename()
    {
        return Filename;
    }

    /**
     * @param Filename The Filename
     */
    public void setFilename(String Filename)
    {
        this.Filename = Filename;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(ProgramType)
                                    .append(Status)
                                    .append(ProgramCode)
                                    .append(PlayedTime)
                                    .append(Filename)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof RoomStatusExt))
        {
            return false;
        }
        RoomStatusExt rhs = ((RoomStatusExt) other);
        return new EqualsBuilder().append(ProgramType, rhs.ProgramType)
                                  .append(Status, rhs.Status)
                                  .append(ProgramCode, rhs.ProgramCode)
                                  .append(PlayedTime, rhs.PlayedTime)
                                  .append(Filename, rhs.Filename)
                                  .isEquals();
    }

}
