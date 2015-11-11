package com.ktvdb.allen.satrok.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ktvdb.allen.satrok.gui.InitActivity;

public class BootBroadcastReceiver extends BroadcastReceiver
{
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    public BootBroadcastReceiver()
    {

    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(action_boot))
        {
            Intent ootStartIntent = new Intent(context, InitActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}
