package com.ktvdb.allen.satrok.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by Allen on 15/9/21.
 */

public class BillInfo
{
    @SerializedName("Succed")
    private String Succed;
    @SerializedName("Des")
    private String Des;
    @SerializedName("Disrate")
    private String Disrate;
    @SerializedName("Discount")
    private String Discount;
    @SerializedName("Service")
    private String Service;
    @SerializedName("Limit")
    private String Limit;
    @SerializedName("Diff")
    private String Diff;
    @SerializedName("Time")
    private String Time;
    @SerializedName("Total")
    private String Total;
    @SerializedName("Paid")
    private String Paid;
    @SerializedName("Nonpay")
    private String Nonpay;



    /**
     * @return The Succed
     */
    public String getSucced()
    {
        return Succed;
    }

    /**
     * @param Succed The Succed
     */
    public void setSucced(String Succed)
    {
        this.Succed = Succed;
    }

    /**
     * @return The Des
     */
    public String getDes()
    {
        return Des;
    }

    /**
     * @param Des The Des
     */
    public void setDes(String Des)
    {
        this.Des = Des;
    }

    /**
     * @return The Disrate
     */
    public String getDisrate()
    {
        return Disrate;
    }

    /**
     * @param Disrate The Disrate
     */
    public void setDisrate(String Disrate)
    {
        this.Disrate = Disrate;
    }

    /**
     * @return The Discount
     */
    public String getDiscount()
    {
        return Discount;
    }

    /**
     * @param Discount The Discount
     */
    public void setDiscount(String Discount)
    {
        this.Discount = Discount;
    }

    /**
     * @return The Service
     */
    public String getService()
    {
        return Service;
    }

    /**
     * @param Service The Service
     */
    public void setService(String Service)
    {
        this.Service = Service;
    }

    /**
     * @return The Limit
     */
    public String getLimit()
    {
        return Limit;
    }

    /**
     * @param Limit The Limit
     */
    public void setLimit(String Limit)
    {
        this.Limit = Limit;
    }

    /**
     * @return The Diff
     */
    public String getDiff()
    {
        return Diff;
    }

    /**
     * @param Diff The Diff
     */
    public void setDiff(String Diff)
    {
        this.Diff = Diff;
    }

    /**
     * @return The Time
     */
    public String getTime()
    {
        return Time;
    }

    /**
     * @param Time The Time
     */
    public void setTime(String Time)
    {
        this.Time = Time;
    }

    /**
     * @return The Total
     */
    public String getTotal()
    {
        return Total;
    }

    /**
     * @param Total The Total
     */
    public void setTotal(String Total)
    {
        this.Total = Total;
    }

    /**
     * @return The Paid
     */
    public String getPaid()
    {
        return Paid;
    }

    /**
     * @param Paid The Paid
     */
    public void setPaid(String Paid)
    {
        this.Paid = Paid;
    }

    /**
     * @return The Nonpay
     */
    public String getNonpay()
    {
        return Nonpay;
    }

    /**
     * @param Nonpay The Nonpay
     */
    public void setNonpay(String Nonpay)
    {
        this.Nonpay = Nonpay;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(Succed)
                                    .append(Des)
                                    .append(Disrate)
                                    .append(Discount)
                                    .append(Service)
                                    .append(Limit)
                                    .append(Diff)
                                    .append(Time)
                                    .append(Total)
                                    .append(Paid)
                                    .append(Nonpay)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof BillInfo))
        {
            return false;
        }
        BillInfo rhs = ((BillInfo) other);
        return new EqualsBuilder().append(Succed, rhs.Succed)
                                  .append(Des, rhs.Des)
                                  .append(Disrate, rhs.Disrate)
                                  .append(Discount, rhs.Discount)
                                  .append(Service, rhs.Service)
                                  .append(Limit, rhs.Limit)
                                  .append(Diff, rhs.Diff)
                                  .append(Time, rhs.Time)
                                  .append(Total, rhs.Total)
                                  .append(Paid, rhs.Paid)
                                  .append(Nonpay, rhs.Nonpay)
                                  .isEquals();
    }


}
