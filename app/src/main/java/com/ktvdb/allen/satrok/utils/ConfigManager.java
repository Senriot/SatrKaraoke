package com.ktvdb.allen.satrok.utils;

import android.net.Uri;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.model.RoomInfo;

import java.text.MessageFormat;

/**
 * Created by Allen on 15/8/27.
 */
public class ConfigManager
{
    private RoomInfo roomInfo;
    private String serverIp      = "127.0.0.1";
    private String erpServerHost = "192.168.1.253";
    private int    erpPort       = 9258;

    public static final int FLAG_YUAN_CHANG = 1;
    public static final int FLAG_BAN_CHANG  = 2;

    private int yuanBanFlag = 2;

    public int getYuanBanFlag()
    {
        return yuanBanFlag;
    }

    public void setYuanBanFlag(int yuanBanFlag)
    {
        this.yuanBanFlag = yuanBanFlag;
    }

    public ConfigManager()
    {
        LogUtils.e("create ConfigManager");
    }

    public String getErpServerHost()
    {
        return erpServerHost;
    }

    public void setErpServerHost(String erpServerHost)
    {
        this.erpServerHost = erpServerHost;
    }

    public int getErpPort()
    {
        return erpPort;
    }

    public void setErpPort(int erpPort)
    {
        this.erpPort = erpPort;
    }

    public RoomInfo getRoomInfo()
    {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo)
    {
        this.roomInfo = roomInfo;
    }

    public String getServerIp()
    {
        return serverIp;
    }

    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getBaseUrl()
    {
        return "http://" + serverIp + ":3379";
    }

    public Uri getMovieImageUri(String url, int w)
    {
        String str = MessageFormat.format("http://{0}:3379/api/movie/image/{1}/{2}",
                                          serverIp,
                                          url,
                                          w);

        return Uri.parse(str);
    }

    public String getSongImageUrl(String config)
    {
        return MessageFormat.format("http://{0}:3379//api/song/image/{1}",
                                    serverIp,
                                    config);
    }

    /**
     * 获取歌星图片URL地址
     *
     * @param img 图片文件
     * @param w   图片宽度
     * @return URL地址
     */
    public String getSingerImageUrl(String img, String w)
    {
        return MessageFormat.format("http://{0}:3379/api/singer/image/{1}/{2}", serverIp, img, w);
    }


    /**
     * 获取专辑图片URL地址
     *
     * @param id 专辑ID
     * @param w  图片宽度
     * @return url
     */
    public String getAlbumImageUrl(String id, String w)
    {
        return MessageFormat.format("http://{0}:3379/api/singer/album/image/{1}/{2}",
                                    serverIp,
                                    id,
                                    w);
    }

    public String getAdvertisementUrl(String file)
    {
        return MessageFormat.format("http://{0}:3379/ad/{1}", serverIp, file);
    }
}
