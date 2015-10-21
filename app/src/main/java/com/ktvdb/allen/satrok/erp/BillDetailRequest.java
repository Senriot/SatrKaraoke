package com.ktvdb.allen.satrok.erp;

/**
 * Created by Allen on 15/9/30.
 */
public class BillDetailRequest
{
//    "Roomno":"399","User":"01","Pass":"1","Type":"0"

    public String Roomno;
    public String User;
    public String Pass;
    public String Type;

    public BillDetailRequest(String roomno, String user, String pass, String type)
    {
        Roomno = roomno;
        User = user;
        Pass = pass;
        Type = type;
    }
}
