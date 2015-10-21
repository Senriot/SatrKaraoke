package com.ktvdb.allen.satrok.presentation;

import android.app.DownloadManager;
import android.content.pm.PackageManager;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.DaggerScope;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.gui.InitActivity;
import com.ktvdb.allen.satrok.model.AppVersion;
import com.ktvdb.allen.satrok.model.HostAddress;
import com.ktvdb.allen.satrok.model.RoomInfo;
import com.ktvdb.allen.satrok.presentation.view.InitView;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.service.SocketService;
import com.ktvdb.allen.satrok.socket.model.Message;
import com.ktvdb.allen.satrok.socket.model.ReceiveMessage;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.GsonUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import cn.finalteam.okhttpfinal.dm.DownloadHttpTask;
import cn.finalteam.okhttpfinal.dm.DownloadInfo;
import cn.finalteam.okhttpfinal.dm.DownloadListener;
import dagger.Lazy;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/27.
 */
@AutoComponent(
        modules = {InitPresentationModel.Module.class},
        dependencies = StarokApplication.class
)
@AutoInjector
@DaggerScope(InitPresentationModel.class)
public class InitPresentationModel
{
    private static InitPresentationModelComponent component;

    private final InitView initView;
    //
    @Inject
    Lazy<RestService> restService;
    @Inject
    ConfigManager     configManager;

    @Inject
    SocketService socketService;

    public InitPresentationModel(InitView initView)
    {
        this.initView = initView;
        EventBus.getDefault().register(this);
        component = DaggerInitPresentationModelComponent.builder()
                                                        .starokApplicationComponent(
                                                                StarokApplication.getAppContext()
                                                                                 .getComponent())
                                                        .module(new Module())
                                                        .build();
        component.inject(this);
        socketService.init();
//        onCheckVersion();
    }

    @Subscriber
    void receiveHostName(HostAddress address)
    {
        configManager.setServerIp(address.getIpAddress());
        configManager.setErpServerHost(address.getIpAddress());
        onCheckVersion();
    }

    @Subscriber(tag = SocketService.DEVICE_INIT)
    void deviceInit(ReceiveMessage message)
    {
        if (!message.succeed)
        {
            initView.onRoomInfoFail(message.content.toString());
        }
        else
        {
            String json = GsonUtil.gson().toJson(message.content);
            RoomInfo roomInfo = GsonUtil.gson().fromJson(json,RoomInfo.class);
            configManager.setServerIp(message.ext.get("serverIp"));
//            configManager.setErpServerHost(message.ext.get("erpIp"));
            configManager.setRoomInfo(roomInfo);
            onCheckVersion();
        }
    }

    public static InitPresentationModelComponent getComponent()
    {
        return component;
    }


    /**
     * 获取服务器APP版本c
     */
    public void onCheckVersion()
    {
        restService.get().getAppversion()
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(this::checkVersion, throwable -> initView.onConnectFail(throwable.getLocalizedMessage()));
    }

    /**
     * 对比版本
     *
     * @param appVersion
     */
    private void checkVersion(AppVersion appVersion)
    {
        int current = getVersion();
        if (Integer.parseInt(appVersion.getValue()) != current)
        {
            //下载更新
            LogUtils.e("需要更新");
            cn.finalteam.okhttpfinal.dm.DownloadManager.getInstance(StarokApplication.getAppContext())
                    .addTask("",new DownloadListener()
                    {
                        @Override
                        public void onProgress(DownloadInfo downloadInfo)
                        {
                        }

                        @Override
                        public void onFinish(DownloadInfo downloadInfo)
                        {
                        }

                        @Override
                        public void onError(DownloadInfo downloadInfo)
                        {
                        }
                    });
        }
        else
        {
            //获取房间信息
//            initRoomInfo();
            initView.startMainActivity();
        }
    }

    /**
     * 获取当前版本
     *
     * @return 版本号
     */
    private int getVersion()
    {
        try
        {
            return StarokApplication.getAppContext()
                                    .getPackageManager()
                                    .getPackageInfo("com.ktvdb.allen.satrok", 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取包厢信息
     */
    public void initRoomInfo()
    {
        socketService.init();
    }

    private void onSoketConnectFail()
    {

    }

    void getRoomInfo()
    {
//        AppObservable.bindActivity((InitActivity) initView, mSocketClient.getRoomInfo())
//                     .observeOn(AndroidSchedulers.mainThread())
//                     .subscribe(roomInfo -> {
//
//                     }, Throwable::printStackTrace);
    }


    @dagger.Module
    public static class Module
    {

    }
}
