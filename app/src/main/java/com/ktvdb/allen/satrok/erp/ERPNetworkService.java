package com.ktvdb.allen.satrok.erp;

import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.net.ConnectException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import io.reactivex.netty.RxNetty;
import io.reactivex.netty.channel.ObservableConnection;
import io.reactivex.netty.client.RxClient;
import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

/**
 * Created by Allen on 15/9/29.
 */
public class ERPNetworkService
{

    RxClient<String, String> mClient;
    ConfigManager            configManager;

    private final FutureConnection                                 futureConnection = new FutureConnection();
    private final FutureTask<ObservableConnection<String, String>> future           = new FutureTask<>(
            futureConnection);

    private static class FutureConnection implements Callable<ObservableConnection<String, String>>
    {

        private ObservableConnection<String, String> connection;

        public void setConnection(final ObservableConnection<String, String> connection)
        {
            if (connection == null)
            {
                throw new IllegalArgumentException();
            }
            if (this.connection != null)
            {
                throw new IllegalStateException("Immutable variable");
            }
            this.connection = connection;
        }

        @Override
        public ObservableConnection<String, String> call() throws Exception
        {
            return connection;
        }
    }

    public ERPNetworkService(ConfigManager configManager)
    {
        this.configManager = configManager;
    }
}
