package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.FragmentLiveDetailBinding;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.util.List;

import rx.Observable;
import rx.android.app.AppObservable;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveDetailFragment extends LevelBaseFragment<FragmentLiveDetailBinding>
{

    LiveProgram mLiveProgram;
    RestService restService;
    private ConfigManager configManager;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_live_detail;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLiveProgram = (LiveProgram) getRequest().getSerializableExtra(LiveProgram.class.getName());
        configManager = StarokApplication.getAppContext().getComponent().configManager();
        restService = StarokApplication.getAppContext().getComponent().restService();
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
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.setLive(mLiveProgram);
        getImageAds();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
