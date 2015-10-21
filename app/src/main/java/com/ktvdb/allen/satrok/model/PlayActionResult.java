package com.ktvdb.allen.satrok.model;

/**
 * Created by Allen on 15/9/30.
 */
public class PlayActionResult
{
    private boolean succeed = true;
    private String msg;

    public boolean isSucceed()
    {
        return succeed;
    }

    public void setSucceed(boolean succeed)
    {
        this.succeed = succeed;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
