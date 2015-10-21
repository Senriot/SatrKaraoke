package com.ktvdb.allen.satrok.erp;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.utils.GsonUtil;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by Allen on 15/9/30.
 */
public class ERPMessage
{
    public static final String SEPARATOR = "\\r\\n";
    public Integer mainCmd;
    public Integer subCmd;
    public Object  content;

    @Override
    public String toString()
    {
        return SEPARATOR + mainCmd + SEPARATOR + subCmd + SEPARATOR + GsonUtil.gson()
                                                                              .toJson(content) + SEPARATOR + 1;
    }

    public static class Buidler
    {
        private ERPMessage message;

        public Buidler()
        {
            this.message = new ERPMessage();
        }

        public Buidler setMainCmd(Integer cmd)
        {
            this.message.mainCmd = cmd;
            return this;
        }

        public Buidler setSubCmd(Integer cmd)
        {
            this.message.subCmd = cmd;
            return this;
        }

        public Buidler setContent(Object o)
        {
            this.message.content = o;
            return this;
        }

        public ByteBuf buidler()
        {
            LogUtils.e(this.message.toString());
            byte[] src       = this.message.toString().getBytes(Charset.forName("GBK"));
            String lenString = String.format("%06d", src.length + 6);

            return Unpooled.buffer()
                           .writeBytes(lenString.getBytes(Charset.forName("GBK")))
                           .writeBytes(src);

        }
    }


}
