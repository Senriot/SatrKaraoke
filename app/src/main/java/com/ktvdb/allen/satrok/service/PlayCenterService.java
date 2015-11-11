package com.ktvdb.allen.satrok.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ktvdb.allen.satrok.DaggerScope;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.event.PlayerReadyedEvent;
import com.ktvdb.allen.satrok.gui.HdmiDisplay;
import com.ktvdb.allen.satrok.gui.PlaybackDisplay;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.PlayActionResult;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.module.PlaybackModule;
import com.ktvdb.allen.satrok.socket.RoomStatusExt;
import com.ktvdb.allen.satrok.socket.RoomStatusRequest;
import com.ktvdb.allen.satrok.socket.RoomStatusResponses;
import com.ktvdb.allen.satrok.socket.model.ReceiveMessage;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedFile;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@AutoComponent(
        modules = {PlaybackModule.class},
        dependencies = StarokApplication.class
)
@AutoInjector
@DaggerScope(PlayCenterService.class)
//@Singleton
public class PlayCenterService extends Service
{
    public static PlayCenterServiceComponent component;
    private       PowerManager.WakeLock      mWakeLock;

    @Inject
    MediaRouter   mMediaRouter;
    @Inject
    AudioManager  mAudioManager;
    @Inject
    WindowManager mWindowManager;

    @Inject
    MediaPlayer mediaPlayer;

    @Inject
    ConfigManager configManager;

    @Inject
    RestService restService;

    private HdmiDisplay mPresentation;

    private final IBinder mBinder = new LocalBinder();

    private class LocalBinder extends Binder
    {
        PlayCenterService getService()
        {
            return PlayCenterService.this;
        }
    }

    public static PlayCenterService getService(IBinder iBinder)
    {
        LocalBinder binder = (LocalBinder) iBinder;
        return binder.getService();
    }

    public PlayCenterService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        component = DaggerPlayCenterServiceComponent.builder()
                                                    .starokApplicationComponent(StarokApplication.getAppContext()
                                                                                                 .getComponent())
                                                    .playbackModule(new PlaybackModule())
                                                    .build();
        component.inject(this);

        EventBus.getDefault().register(this);
        mMediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, mMediaRouterCallback);
        updatePresentation();
//        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 8, 0);
        hideSystemBar();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Starok");
    }

    private void hideSystemBar()
    {
        ImageView                  imageView    = new ImageView(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = 0;
        layoutParams.type = 2010;
        layoutParams.flags = 8658992;
        mWindowManager.addView(imageView, layoutParams);
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        imageView.setSystemUiVisibility(flag);
    }


    private void updatePresentation()
    {
        MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(
                MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;
        if (mPresentation != null && mPresentation.getDisplay() != presentationDisplay)
        {
            mPresentation.dismiss();
            mPresentation = null;
        }

        if (mPresentation == null && presentationDisplay != null)
        {

            mPresentation = new HdmiDisplay(this, presentationDisplay);
            mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mPresentation.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
            try
            {
                mPresentation.show();
            }
            catch (WindowManager.InvalidDisplayException ex)
            {
                mPresentation = null;
            }
        }
    }

    public static class Client
    {
        public static final String TAG = "PlaybackService.Client";

        @MainThread
        public interface Callback
        {
            void onConnected(PlayCenterService service);

            void onDisconnected();
        }

        private boolean mBound = false;
        private final Callback mCallback;
        private final Context  mContext;

        private final ServiceConnection mServiceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder)
            {
                Log.d(TAG, "Service Connected");
                if (!mBound)
                {
                    return;
                }

                final PlayCenterService service = PlayCenterService.getService(iBinder);
                if (service != null)
                {
                    mCallback.onConnected(service);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                Log.d(TAG, "Service Disconnected");
                mCallback.onDisconnected();
            }
        };

        private static Intent getServiceIntent(Context context)
        {
            return new Intent(context, PlayCenterService.class);
        }

        private static void startService(Context context)
        {
            context.startService(getServiceIntent(context));
        }

        private static void stopService(Context context)
        {
            context.stopService(getServiceIntent(context));
        }

        public Client(Context context, Callback callback)
        {
            if (context == null || callback == null)
            {
                throw new IllegalArgumentException("Context and callback can't be null");
            }
            mContext = context;
            mCallback = callback;
        }

        @MainThread
        public void connect()
        {
            if (mBound)
            {
                throw new IllegalStateException("already connected");
            }
            startService(mContext);
            mBound = mContext.bindService(getServiceIntent(mContext),
                                          mServiceConnection,
                                          BIND_AUTO_CREATE);
        }

        @MainThread
        public void disconnect()
        {
            if (mBound)
            {
                mBound = false;
                mContext.unbindService(mServiceConnection);
            }
        }

        public static void restartService(Context context)
        {
            stopService(context);
            startService(context);
        }
    }


    private final MediaRouter.SimpleCallback mMediaRouterCallback =
            new MediaRouter.SimpleCallback()
            {
                @Override
                public void onRouteSelected(MediaRouter router,
                                            int type,
                                            MediaRouter.RouteInfo info)
                {
                    updatePresentation();
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteUnselected(MediaRouter router,
                                              int type,
                                              MediaRouter.RouteInfo info)
                {
                    updatePresentation();
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRoutePresentationDisplayChanged(MediaRouter router,
                                                              MediaRouter.RouteInfo info)
                {
                    updatePresentation();
                }

                @Override
                public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info)
                {
                    super.onRouteAdded(router, info);
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info)
                {
                    super.onRouteRemoved(router, info);
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo info)
                {
                    super.onRouteChanged(router, info);
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteGrouped(MediaRouter router,
                                           MediaRouter.RouteInfo info,
                                           MediaRouter.RouteGroup group,
                                           int index)
                {
                    super.onRouteGrouped(router, info, group, index);
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteUngrouped(MediaRouter router,
                                             MediaRouter.RouteInfo info,
                                             MediaRouter.RouteGroup group)
                {
                    super.onRouteUngrouped(router, info, group);
                    LogUtils.e("onRouteSelected");
                }

                @Override
                public void onRouteVolumeChanged(MediaRouter router, MediaRouter.RouteInfo info)
                {
                    super.onRouteVolumeChanged(router, info);
                    LogUtils.e("onRouteSelected");
                }
            };


    @Subscriber(tag = "GET_ROOM_STATUS")
    public void roomStatus(ReceiveMessage message)
    {
        Map<String, String> map       = (Map<String, String>) message.content;
        String              messageId = map.get("msgID");
        takeSnapShot(messageId);
    }

    @Subscriber
    public void playerReadyed(PlayerReadyedEvent event)
    {
        if (mediaPlayer.getTempPlayList() == null)
        {
            mediaPlayer.mRecordService.getTempPlayList(0, songPageResponse -> {
                mediaPlayer.setTempPlayList(songPageResponse);
                mediaPlayer.onCut();
            });
        }
        else if (mediaPlayer.getMedia() != null && mediaPlayer.mPlayer.getPlayWhenReady() && mediaPlayer.mPlayer
                .getPlaybackState() == 1)
        {
            mediaPlayer.goOnPlay();
        }
    }

    public Bitmap bitmap()
    {
        return mPresentation.screenshot();
    }

    public void takeSnapShot(String messageId)
    {
        if (mPresentation.isShowing())
        {
            Bitmap layoutBitmap = mPresentation.screenshot();
            if (mediaPlayer.getMedia() != null)
            {
                getScreenshot().subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(bitmap -> {
                                   bitmap = Bitmap.createScaledBitmap(bitmap,
                                                                      layoutBitmap.getWidth(),
                                                                      layoutBitmap.getHeight(),
                                                                      true);
                                   Rect r = new Rect(0,
                                                     0,
                                                     layoutBitmap.getWidth(),
                                                     layoutBitmap.getHeight());
                                   Paint paint = new Paint();
                                   Canvas canvas = new Canvas(layoutBitmap);
                                   paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
                                   canvas.drawBitmap(bitmap, r, r, paint);
                                   canvas.save(Canvas.ALL_SAVE_FLAG);
                                   canvas.restore();

                                   updateImage(layoutBitmap, messageId);
                               });
            }
            else
            {
                updateImage(layoutBitmap, messageId);
            }
        }
    }

    private void updateImage(Bitmap bitmap, String messageId)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);

        RoomStatusResponses roomStatusResponses = new RoomStatusResponses();
        roomStatusResponses.setMsgID(messageId);
        roomStatusResponses.setDeviceCode(StarokApplication.getAppContext()
                                                           .uuid());
        roomStatusResponses.setMsg("终端实时状态回传成功");
        RoomStatusExt ext = new RoomStatusExt();
        ext.setProgramType("1");
        ext.setProgramCode(mediaPlayer.getMedia() == null ? "" : mediaPlayer.getMedia().getCode());
        ext.setStatus("1");
        ext.setFilename(UUID.randomUUID().toString() + ".jpg");
        ext.setPlayedTime(TimeUtils.stringForTime(mediaPlayer.getCurrentPosition()));
        roomStatusResponses.getExt().add(ext);

        @SuppressLint("SdCardPath") File file = new File("/sdcard/starok" + "/" + ext.getFilename());
        try
        {
            FileUtils.writeByteArrayToFile(file, baos.toByteArray());
            if (file.exists())
            {
                TypedFile typedFile = new TypedFile("image/jpg", file);
                restService.uploadImage(roomStatusResponses, typedFile)
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribeOn(Schedulers.io())
                           .onErrorResumeNext(Observable.<PlayActionResult>empty())
                           .subscribe((PlayActionResult object) -> {
                               file.delete();
                           });
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    Observable<Bitmap> getScreenshot()
    {
        return Observable.create(new Observable.OnSubscribe<Bitmap>()
        {
            @Override
            public void call(rx.Subscriber<? super Bitmap> subscriber)
            {
                String url = String.format("http://" + configManager.getServerIp() + ":3379/resource/song/screenshot/%s/%.3f",
                                           mediaPlayer.getMedia().getId(),
                                           mediaPlayer.getCurrentPosition() / 1000.000);


                try
                {
                    Bitmap bitmap = Picasso.with(PlayCenterService.this).load(Uri.parse(url)).get();
                    subscriber.onNext(bitmap);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}
