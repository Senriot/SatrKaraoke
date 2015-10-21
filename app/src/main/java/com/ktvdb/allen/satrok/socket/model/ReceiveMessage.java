package com.ktvdb.allen.satrok.socket.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 15/9/29.
 */
public class ReceiveMessage
{
    public String  cmd;
    public Object  content;
    public Boolean succeed;
    public Map<String, String> ext = new HashMap<>();

    @Override
    public String toString()
    {
        return "ReceiveMessage{" +
                "cmd='" + cmd + '\'' +
                ", content='" + content + '\'' +
                ", succeed=" + succeed +
                '}';
    }
}
