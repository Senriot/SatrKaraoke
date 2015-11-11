package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSongCategoryBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.AdvertisementImageAdapter;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.gui.widget.NewokCardView;
import com.ktvdb.allen.satrok.gui.widget.SongCategoryGrid;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
@FragmnetTitle("分类")
public class SongCategoryFragment extends LevelBaseFragment<FragmentSongCategoryBinding>
{

    @Inject
    RestService   restService;
    @Inject
    ConfigManager configManager;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_song_category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
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
                     .subscribe(mBinding.adPageView::setAdvertisements);
    }

    @OnClick({R.id.btn_NianDai, R.id.btn_ZhengNengLiang, R.id.btn_ShaiShi, R.id.btn_QingGan,
                     R.id.btn_WuQu, R.id.btn_WangLuo, R.id.btn_YingShi, R.id.btn_DiYu, R.id.btn_MinGe})
    void showNianDai(View view)
    {
        if (ViewUtils.isFastDoubleClick()) return;
        SongCategoryGrid cardView = (SongCategoryGrid) view;
        Request request = new Request(SongCategoryListFragment.class).putExtra("title",
                                                                               cardView.cnName)
                                                                     .putExtra("categoryID",
                                                                               (String) cardView.getTag());

        startFragment(request);
    }

}
