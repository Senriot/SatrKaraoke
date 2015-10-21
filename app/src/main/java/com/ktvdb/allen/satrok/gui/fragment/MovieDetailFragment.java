package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.FragmentMovieDetailBinding;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends LevelBaseFragment<FragmentMovieDetailBinding>
{

    Movie mMovie;

    MediaPlayer mPlayer;
    RestService restService;
    private ConfigManager configManager;


    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_movie_detail;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        configManager = StarokApplication.getAppContext().getComponent().configManager();
        restService = StarokApplication.getAppContext().getComponent().restService();
        mMovie = (Movie) getRequest().getSerializableExtra(Movie.class.getName());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.setMovie(mMovie);
        Uri uri = StarokApplication.getAppContext()
                                   .getComponent()
                                   .configManager()
                                   .getMovieImageUri(mMovie.getImg(), 400);
        mBinding.liveImg.setImageURI(uri);
        getImageAds();
    }

    /**
     * 图片广告
     */
    private void getImageAds()
    {
        AppObservable.bindFragment(this,
                                   restService.geLeftImageAds(configManager.getRoomInfo()
                                                                           .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(advertisements -> {
                         mBinding.adPageView.addAds(advertisements);
                         mBinding.adPageView.startScroll();
                     });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_play)
    void onPlay()
    {
        mPlayer.onPlayMovie(mMovie);
//        mContext.getPlaybackService().onPlayMovie(mMovie);
    }

    @OnClick(R.id.btn_YuYue)
    void onAddList()
    {
        mPlayer.addLastMedia(mMovie);
//        mContext.getPlaybackService().addLast(mMovie);
    }
}
