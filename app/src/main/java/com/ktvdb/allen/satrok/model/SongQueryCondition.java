package com.ktvdb.allen.satrok.model;


/**
 * Created by Allen on 15/7/14.
 */
public class SongQueryCondition
{
    private int page = 0;
    private int size = 48;


    public enum SongCategory
    {
        None,
        Singer,
        Language,//语种
        NewSong,//新歌
        WordCount,//字数
        PinYin,
        GQSSND, //年代
        ZNLFL, //正能量
        ZYJMLX,
        QGLX,
        WQLX,
        WLLX,
        YSFL,
        DYDQ,
        MGLX
    }

    /**
     * 歌星ID
     */
    private String singerId;
    /**
     * 搜索关键字
     */
    private String keyword = "";
    /**
     * 语种id
     */
    private String languageId;

    /**
     * 新歌
     */
    private boolean isNew;
    /**
     * 字数
     */
    private Integer wordCount;
    /**
     * 排序字段
     */
    private String  sort;

    private Direction direction;

    private SongCategory songCategory = SongCategory.None;

    private String categoryID;

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

    public String getSingerId()
    {
        return singerId;
    }

    public void setSingerId(String singerId)
    {
        this.singerId = singerId;
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

    public String getLanguageId()
    {
        return languageId;
    }

    public void setLanguageId(String languageId)
    {
        this.languageId = languageId;
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setIsNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    public Integer getWordCount()
    {
        return wordCount;
    }

    public void setWordCount(Integer wordCount)
    {
        this.wordCount = wordCount;
    }

    public SongCategory getSongCategory()
    {
        return songCategory;
    }

    public void setSongCategory(SongCategory songCategory)
    {
        this.songCategory = songCategory;
    }

    public String getCategoryID()
    {
        return categoryID;
    }

    public void setCategoryID(String categoryID)
    {
        this.categoryID = categoryID;
    }
}
