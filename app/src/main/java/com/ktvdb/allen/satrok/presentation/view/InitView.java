package com.ktvdb.allen.satrok.presentation.view;

/**
 * Created by Allen on 15/8/27.
 */
public interface InitView
{

    void startMainActivity();

    void onConnectFail(String msg);

    void onRoomInfoFail(String msg);
}
