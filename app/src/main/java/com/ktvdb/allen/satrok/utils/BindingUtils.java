package com.ktvdb.allen.satrok.utils;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.SongInfoPanelBinding;
import com.ktvdb.allen.satrok.gui.widget.SongCategoryGrid;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.service.RestService;

import net.qiujuer.genius.blur.StackBlur;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/30.
 */
public class BindingUtils
{
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(SimpleDraweeView draweeView, String url)
    {
        if (StringUtils.isNoneBlank(url))
        {
            draweeView.setImageURI(Uri.parse(url));
        }
    }

    @BindingAdapter({"bind:songImage"})
    public static void songImage(SimpleDraweeView draweeView, String id)
    {
        ConfigManager configManager = StarokApplication.getAppContext()
                                                       .getComponent()
                                                       .configManager();
        int width  = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        draweeView.measure(width, height);
        String url = configManager.getSongImageUrl(id);
        draweeView.setImageURI(Uri.parse(url));
    }

    @BindingAdapter({"bind:singerItemImage"})
    public static void singerItemImage(SimpleDraweeView draweeView, String file)
    {
        loadSingImage(draweeView, file, "300");
    }

    @BindingAdapter({"bind:singerBigImage"})
    public static void singerBigImage(SimpleDraweeView draweeView, String file)
    {
        loadSingImage(draweeView, file, "1000");
    }

    @BindingAdapter({"bind:singerBlurImage"})
    public static void singerBlurImage(SimpleDraweeView view, String file)
    {
        if (!StringUtils.isBlank(file))
        {
            ConfigManager configManager = StarokApplication.getAppContext()
                                                           .getComponent()
                                                           .configManager();

            String url = configManager.getSingerImageUrl(file, "1000");

            stackBlurImage(view, url);
        }
    }

    @BindingAdapter({"bind:albumItemImage"})
    public static void albumItemImage(SimpleDraweeView draweeView, String albumID)
    {
        loadAlbumImage(draweeView, albumID, "300");
    }

    @BindingAdapter({"bind:albumBlurImage"})
    public static void albumBlurImage(SimpleDraweeView view, String albumID)
    {
        ConfigManager configManager = StarokApplication.getAppContext()
                                                       .getComponent()
                                                       .configManager();

        String url = configManager.getAlbumImageUrl(albumID, "1000");
        stackBlurImage(view, url);
    }

    @BindingAdapter({"bind:albumBigImage"})
    public static void albumBigImage(SimpleDraweeView draweeView, String albumID)
    {
        loadAlbumImage(draweeView, albumID, "1000");
    }

    public static void loadAlbumImage(SimpleDraweeView draweeView, String albumID, String w)
    {
        ConfigManager config = StarokApplication.getAppContext().getComponent().configManager();
        String        url    = config.getAlbumImageUrl(albumID, w);
        draweeView.setImageURI(Uri.parse(url));
    }

    public static void loadSingImage(SimpleDraweeView draweeView, String file, String w)
    {
        if (!StringUtils.isBlank(file))
        {
            ConfigManager configManager = StarokApplication.getAppContext()
                                                           .getComponent()
                                                           .configManager();

            String url = configManager.getSingerImageUrl(file, w);
            LogUtils.e(url);
            draweeView.setImageURI(Uri.parse(url));

        }
        else
        {
            draweeView.setImageURI(Uri.parse(
                    "android.resource://" + StarokApplication.getAppContext().getPackageName() +
                            "/" + R.drawable.singer_def));
        }
    }

    @BindingAdapter({"bind:blurImage"})
    public static void stackBlurImage(SimpleDraweeView draweeView, String url)
    {
        if (url != null)
        {
            Postprocessor redMeshPostprocessor = new BasePostprocessor()
            {
                @Override
                public String getName()
                {
                    return "redMeshPostprocessor";
                }

                @Override
                public void process(Bitmap bitmap)
                {
                    StackBlur.blurNatively(bitmap, 60, true);
                }
            };

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                                                      .setPostprocessor(redMeshPostprocessor)
                                                      .build();

            PipelineDraweeController controller = (PipelineDraweeController)
                    Fresco.newDraweeControllerBuilder()
                          .setImageRequest(request)
                          .setOldController(draweeView.getController())
                          .build();
            draweeView.setController(controller);
        }
    }

    @BindingAdapter({"bind:adImage"})
    public static void adImage(SimpleDraweeView draweeView, String file)
    {
        if (StringUtils.isNoneBlank(file))
        {
            ConfigManager config = StarokApplication.getAppContext().getComponent().configManager();
            draweeView.setImageURI(Uri.parse(config.getAdvertisementUrl(file)));
        }
    }

    @BindingAdapter({"bind:seekBarMax"})
    public static void seekBarMax(SeekBar seekBar, int max)
    {
        seekBar.setMax(max);
    }

    @BindingAdapter({"bind:seekBarProgress"})
    public static void seekBarProgress(SeekBar seekBar, int progress)
    {
        seekBar.setProgress(progress);
    }

    @BindingAdapter({"bind:categoryContent"})
    public static void categoryContent(SongCategoryGrid grid, String tag)
    {
        RestService service = StarokApplication.getAppContext().getComponent().restService();
        service.getSongCategory(tag).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
               .subscribe(sysDictionaries -> {
                   StringBuilder content = new StringBuilder("");
                   for (SysDictionary d : sysDictionaries)
                   {
                       if (content.length() > 0)
                       {
                           content.append("、").append(d.getDictName());
                       }
                       else
                       {
                           content.append(d.getDictName());
                       }
                   }
                   grid.setContext(content.toString());
               });
    }

    public static void showSongDetail(View view, Song song)
    {
        final SongInfoPanelBinding infoPanelBinding = DataBindingUtil.inflate(
                LayoutInflater.from(view.getContext()),
                R.layout.song_info_panel,
                null,
                false);
        infoPanelBinding.setSong(song);
        int width  = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        infoPanelBinding.getRoot().measure(width, height);
        PopupWindow pw = new PopupWindow(view.getContext());
        pw.setFocusable(true);
        pw.setWidth(infoPanelBinding.getRoot().getMeasuredWidth());
        pw.setHeight(infoPanelBinding.getRoot().getMeasuredHeight());
        pw.setContentView(infoPanelBinding.getRoot());
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.update();
        pw.showAsDropDown(view, -view.getWidth(), -view.getHeight(), Gravity.RIGHT);
    }

}