package com.ktvdb.allen.satrok.gui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.presentation.InitPresentationModel;
import com.ktvdb.allen.satrok.presentation.view.InitView;

import java.util.UUID;


public class InitActivity extends AppCompatActivity implements InitView
{

    InitPresentationModel presentationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        presentationModel = new InitPresentationModel(this);
        setContentView(R.layout.activity_init);
    }


    @Override
    public void startMainActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        finish();
    }

    @Override
    public void onConnectFail(String msg)
    {
        AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("重试", (dialog, id) -> {
                    presentationModel.onCheckVersion();
                })
                .setNegativeButton("退出", (dialog, id) -> {
                    StarokApplication.getAppContext().onTerminate();
                })
                .create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    @Override
    public void onRoomInfoFail(String msg)
    {
        AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("重试", (dialog, id) -> {
                    presentationModel.initRoomInfo();
                })
                .setNegativeButton("退出", (dialog, id) -> {
                    StarokApplication.getAppContext().onTerminate();
                })
                .create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }
}
