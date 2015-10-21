package com.ktvdb.allen.satrok.service;

import android.content.Context;
import android.support.annotation.MainThread;

import java.util.ArrayList;


/**
 * Created by Allen on 15/7/8.
 */
public class PlayServiceHelper
{
    private ArrayList<PlayCenterService.Client.Callback> mFragmentCallbacks = new ArrayList<PlayCenterService.Client.Callback>();
    final private PlayCenterService.Client.Callback mActivityCallback;
    private       PlayCenterService.Client          mClient;
    protected     PlayCenterService                 mService;

    public PlayServiceHelper(Context context, PlayCenterService.Client.Callback activityCallback)
    {
        mClient = new PlayCenterService.Client(context, mClientCallback);
        mActivityCallback = activityCallback;
    }

    @MainThread
    public void registerFragment(PlayCenterService.Client.Callback connectCb)
    {
        if (connectCb == null)
        {
            throw new IllegalArgumentException("connectCb can't be null");
        }
        mFragmentCallbacks.add(connectCb);
        if (mService != null)
        {
            connectCb.onConnected(mService);
        }
    }

    @MainThread
    public void unregisterFragment(PlayCenterService.Client.Callback connectCb)
    {
        if (mService != null)
        {
            connectCb.onDisconnected();
        }
        mFragmentCallbacks.remove(connectCb);
    }

    @MainThread
    public void onStart()
    {
        mClient.connect();
    }

    @MainThread
    public void onStop()
    {
        mClientCallback.onDisconnected();
        mClient.disconnect();
    }

    private final PlayCenterService.Client.Callback mClientCallback = new PlayCenterService.Client.Callback()
    {
        @Override
        public void onConnected(PlayCenterService service)
        {
            mService = service;
            mActivityCallback.onConnected(service);
            for (PlayCenterService.Client.Callback connectCb : mFragmentCallbacks)
                connectCb.onConnected(mService);
        }

        @Override
        public void onDisconnected()
        {
            mService = null;
            mActivityCallback.onDisconnected();
            for (PlayCenterService.Client.Callback connectCb : mFragmentCallbacks)
                connectCb.onDisconnected();
        }
    };


}
