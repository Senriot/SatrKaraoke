package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/8/27.
 */
public class AppVersion
{

    /**
     * id : 7
     * name : app_version
     * value : 1.0.0
     * remake : app版本
     */

    private int id;
    private String name;
    private String value;
    private String remake;

    public void setId(int id) { this.id = id;}

    public void setName(String name) { this.name = name;}

    public void setValue(String value) { this.value = value;}

    public void setRemake(String remake) { this.remake = remake;}

    public int getId() { return id;}

    public String getName() { return name;}

    public String getValue() { return value;}

    public String getRemake() { return remake;}
}
