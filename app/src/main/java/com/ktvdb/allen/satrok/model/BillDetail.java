
package com.ktvdb.allen.satrok.model;

import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class BillDetail
{

    @SerializedName("Succed")
    private String Succed;
    @SerializedName("Des")
    private String Des;
    @SerializedName("Rowcount")
    private String Rowcount;
    @SerializedName("Data")
    private List<Datum> Data = new ArrayList<Datum>();

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
     * @return The Rowcount
     */
    public String getRowcount()
    {
        return Rowcount;
    }

    /**
     * @param Rowcount The Rowcount
     */
    public void setRowcount(String Rowcount)
    {
        this.Rowcount = Rowcount;
    }

    /**
     * @return The Data
     */
    public List<Datum> getData()
    {
        return Data;
    }

    /**
     * @param Data The Data
     */
    public void setData(List<Datum> Data)
    {
        this.Data = Data;
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
                                    .append(Rowcount)
                                    .append(Data)
                                    .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if ((other instanceof BillDetail) == false)
        {
            return false;
        }
        BillDetail rhs = ((BillDetail) other);
        return new EqualsBuilder().append(Succed, rhs.Succed)
                                  .append(Des, rhs.Des)
                                  .append(Rowcount, rhs.Rowcount)
                                  .append(Data, rhs.Data)
                                  .isEquals();
    }

    public static class Datum
    {

        @SerializedName("Id")
        private String Id;
        @SerializedName("Name")
        private String Name;
        @SerializedName("Unit")
        private String Unit;
        @SerializedName("Price")
        private String Price;
        @SerializedName("Num")
        private String Num;
        @SerializedName("Type")
        private String Type;

        private float totalPrice;

        public float getTotalPrice()
        {
            Price = Price.replace(",", "");
            return Integer.parseInt(Num) * Float.parseFloat(Price);
        }


        /**
         * @return The Id
         */
        public String getId()
        {
            return Id;
        }

        /**
         * @param Id The Id
         */
        public void setId(String Id)
        {
            this.Id = Id;
        }

        /**
         * @return The Name
         */
        public String getName()
        {
            return Name;
        }

        /**
         * @param Name The Name
         */
        public void setName(String Name)
        {
            this.Name = Name;
        }

        /**
         * @return The Unit
         */
        public String getUnit()
        {
            return Unit;
        }

        /**
         * @param Unit The Unit
         */
        public void setUnit(String Unit)
        {
            this.Unit = Unit;
        }

        /**
         * @return The Price
         */
        public String getPrice()
        {
            return Price;
        }

        /**
         * @param Price The Price
         */
        public void setPrice(String Price)
        {
            this.Price = Price;
        }

        /**
         * @return The Num
         */
        public String getNum()
        {
            return Num;
        }

        /**
         * @param Num The Num
         */
        public void setNum(String Num)
        {
            this.Num = Num;
        }

        /**
         * @return The Type
         */
        public String getType()
        {
            return Type;
        }

        /**
         * @param Type The Type
         */
        public void setType(String Type)
        {
            this.Type = Type;
        }

        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this);
        }

        @Override
        public int hashCode()
        {
            return new HashCodeBuilder().append(Id)
                                        .append(Name)
                                        .append(Unit)
                                        .append(Price)
                                        .append(Num)
                                        .append(Type)
                                        .toHashCode();
        }

        @Override
        public boolean equals(Object other)
        {
            if (other == this)
            {
                return true;
            }
            if ((other instanceof Datum) == false)
            {
                return false;
            }
            Datum rhs = ((Datum) other);
            return new EqualsBuilder().append(Id, rhs.Id)
                                      .append(Name, rhs.Name)
                                      .append(Unit, rhs.Unit)
                                      .append(Price, rhs.Price)
                                      .append(Num, rhs.Num)
                                      .append(Type, rhs.Type)
                                      .isEquals();
        }

    }

}
