package com.ktvdb.allen.satrok;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.ktvdb.allen.satrok.bind.PlayHubModel;
import com.ktvdb.allen.satrok.erp.ERPNetworkService;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.PlayRecordService;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.service.SocketService;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.GsonUtil;

import java.util.UUID;

import javax.inject.Singleton;

import autodagger.AutoComponent;
import autodagger.AutoExpose;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Allen on 15/8/27.
 */
@AutoComponent(modules = StarokApplication.Module.class)
@Singleton
public class StarokApplication extends Application
{
    private static StarokApplication instance;

    private StarokApplicationComponent component;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
        component = DaggerStarokApplicationComponent.create();
    }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        System.exit(0);
    }

    public String uuid()
    {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String           tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(),
                                                                    android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                                   ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        LogUtils.e(uniqueId);
        return uniqueId;
    }

    public static StarokApplication getAppContext()
    {
        return instance;
    }

    public StarokApplicationComponent getComponent()
    {
        return component;
    }

    @dagger.Module
    public static class Module
    {
        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        ConfigManager providesConfigManager()
        {
            return new ConfigManager();
        }

        @Provides
        @Singleton
        public RestAdapter providesRestAdapter(ConfigManager configManager)
        {
            return new RestAdapter.Builder()
                    .setEndpoint(configManager.getBaseUrl())
                    .setConverter(new GsonConverter(GsonUtil.gson()))
                    .build();
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        RestService providesRestService(RestAdapter adapter)
        {
            return adapter.create(RestService.class);
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        AudioManager providesAudioManager()
        {
            return (AudioManager) StarokApplication.getAppContext()
                                                   .getSystemService(Context.AUDIO_SERVICE);
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        WindowManager providesWindowManager()
        {
            return (WindowManager) StarokApplication.getAppContext()
                                                    .getSystemService(Context.WINDOW_SERVICE);
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        MediaRouter providesMediaRouter()
        {
            return (MediaRouter) StarokApplication.getAppContext()
                                                  .getSystemService(Context.MEDIA_ROUTER_SERVICE);
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        MediaPlayer providesMediaPlayer()
        {
            return new MediaPlayer();
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        PlayHubModel providesPlayHubModel()
        {
            return new PlayHubModel();
        }

        @Provides
        @Singleton
        android.media.MediaPlayer mediaPlayer()
        {
            return new android.media.MediaPlayer();
        }


        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        PlayRecordService providesPlayRecordService(RestService restService)
        {
            return new PlayRecordService(restService);
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        SocketService providesSocketService()
        {
            return new SocketService();
        }

        @Provides
        @AutoExpose(StarokApplication.class)
        @Singleton
        ERPNetworkService providesERPNetworkService(ConfigManager configManager)
        {
            return new ERPNetworkService(configManager);
        }
    }
}
