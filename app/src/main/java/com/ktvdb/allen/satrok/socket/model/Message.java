package com.ktvdb.allen.satrok.socket.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 15/9/29.
 */
public class Message<T>
{
    private String cmd;
    private T      content;
    private Map<String, Object> ext = new HashMap<>();

    public String getCmd()
    {
        return cmd;
    }

    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }

    public T getContent()
    {
        return content;
    }

    public void setContent(T content)
    {
        this.content = content;
    }

    public Map<String, Object> getExt()
    {
        return ext;
    }

    public void setExt(Map<String, Object> ext)
    {
        this.ext = ext;
    }
}
