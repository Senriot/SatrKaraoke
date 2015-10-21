
package com.ktvdb.allen.satrok.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class PageResponse<T> implements Serializable
{

    private Boolean last;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Boolean first;
    private Integer numberOfElements;

    private List<T> content;

    public List<T> getContent()
    {
        return content;
    }

    public void setContent(List<T> content)
    {
        this.content = content;
    }

    /**
     * @return The last
     */
    public Boolean getLast()
    {
        return last;
    }

    /**
     * @param last The last
     */
    public void setLast(Boolean last)
    {
        this.last = last;
    }

    /**
     * @return The totalPages
     */
    public Integer getTotalPages()
    {
        return totalPages;
    }

    /**
     * @param totalPages The totalPages
     */
    public void setTotalPages(Integer totalPages)
    {
        this.totalPages = totalPages;
    }

    /**
     * @return The totalElements
     */
    public Integer getTotalElements()
    {
        return totalElements;
    }

    /**
     * @param totalElements The totalElements
     */
    public void setTotalElements(Integer totalElements)
    {
        this.totalElements = totalElements;
    }

    /**
     * @return The size
     */
    public Integer getSize()
    {
        return size;
    }

    /**
     * @param size The size
     */
    public void setSize(Integer size)
    {
        this.size = size;
    }

    /**
     * @return The number
     */
    public Integer getNumber()
    {
        return number;
    }

    /**
     * @param number The number
     */
    public void setNumber(Integer number)
    {
        this.number = number;
    }

    /**
     * @return The first
     */
    public Boolean getFirst()
    {
        return first;
    }

    /**
     * @param first The first
     */
    public void setFirst(Boolean first)
    {
        this.first = first;
    }

    /**
     * @return The numberOfElements
     */
    public Integer getNumberOfElements()
    {
        return numberOfElements;
    }

    /**
     * @param numberOfElements The numberOfElements
     */
    public void setNumberOfElements(Integer numberOfElements)
    {
        this.numberOfElements = numberOfElements;
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append(last)
                .append(totalPages)
                .append(totalElements)
                .append(size)
                .append(number)
                .append(first)
                .append(numberOfElements)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof PageResponse))
        {
            return false;
        }
        PageResponse rhs = ((PageResponse) other);
        return new EqualsBuilder()
                .append(last, rhs.last)
                .append(totalPages,
                        rhs.totalPages)
                .append(totalElements, rhs.totalElements)
                .append(size, rhs.size)
                .append(number, rhs.number)
                .append(first, rhs.first)
                .append(numberOfElements, rhs.numberOfElements)
                .isEquals();
    }

}
