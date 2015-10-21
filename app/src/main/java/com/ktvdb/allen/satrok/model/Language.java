package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/7/14.
 */
public class Language
{
    private int code;
    private String name;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (code != language.code) return false;
        if (name != null ? !name.equals(language.name) : language.name != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = code;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
