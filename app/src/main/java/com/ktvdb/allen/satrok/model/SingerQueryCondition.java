package com.ktvdb.allen.satrok.model;


/**
 * Created by Allen on 15/7/13.
 */
public class SingerQueryCondition
{
    public enum SingerCategory
    {
        None, // 全部
        SingerType
    }

    private int page = 0;
    private int size = 48;


    private String type;
    private String keyword = "";
    private String    sort;
    private Direction direction;
    private SingerCategory singerCategory = SingerCategory.None;


    public SingerQueryCondition()
    {
    }

    public SingerQueryCondition(String type,
                                String sort,
                                Direction direction,
                                SingerCategory singerCategory)
    {
        this.type = type;
        this.sort = sort;
        this.direction = direction;
        this.singerCategory = singerCategory;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getKeyword()
    {
        return keyword;
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public SingerCategory getSingerCategory()
    {
        return singerCategory;
    }

    public void setSingerCategory(SingerCategory mSingerCategory)
    {
        this.singerCategory = mSingerCategory;
    }

}
