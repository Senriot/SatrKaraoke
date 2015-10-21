package com.ktvdb.allen.satrok.model;


/**
 * Created by Allen on 15/7/20.
 */
public class SysDictionary
{
    private String id;
    private String dictName;
    private String parentId;
    private String dictNO;
    private int    sort;
    private int    status;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getDictNO()
    {
        return dictNO;
    }

    public void setDictNO(String dictNO)
    {
        this.dictNO = dictNO;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysDictionary that = (SysDictionary) o;

        if (sort != that.sort) return false;
        if (status != that.status) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (dictName != null ? !dictName.equals(that.dictName) : that.dictName != null)
            return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dictName != null ? dictName.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + sort;
        result = 31 * result + status;
        return result;
    }
}
