package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/7/11.
 */
public class MovieQueryCondition
{
    private Integer page = 0;
    private Integer size = 49;
    private String typeCode;

    public Integer getPage()
    {
        return page;
    }

    public void setPage(Integer page)
    {
        this.page = page;
    }

    public Integer getSize()
    {
        return size;
    }

    public void setSize(Integer size)
    {
        this.size = size;
    }

    public String getTypeCode()
    {
        return typeCode;
    }

    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
}
