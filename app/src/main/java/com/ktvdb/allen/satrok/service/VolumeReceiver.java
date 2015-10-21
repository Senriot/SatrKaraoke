package com.ktvdb.allen.satrok.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.event.VolumeChangedEvent;

import org.simple.eventbus.EventBus;

public class VolumeReceiver extends BroadcastReceiver
{

    AudioManager audioManager;

    public VolumeReceiver()
    {
        audioManager = (AudioManager) StarokApplication.getAppContext()
                                                       .getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.e(volume);
        EventBus.getDefault().post(new VolumeChangedEvent(volume));
    }
}
