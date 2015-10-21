package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentMainBinding;
import com.ktvdb.allen.satrok.gui.adapters.AdvertisementImageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.AlbumPageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.presentation.MainPagePresentation;
import com.ktvdb.allen.satrok.presentation.view.MainPageView;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends AbstractFragment<FragmentMainBinding> implements MainPageView,
                                                                                   OnItemClickListener
{

    MainPagePresentation mPresentation;

    public MainFragment()
    {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mPresentation = new MainPagePresentation(this);
    }

    @Override
    public void onPlayRecommendAlbums(List<Album> albums)
    {
        AlbumPageAdapter albumPageAdapter = new AlbumPageAdapter(albums, getActivity());
        albumPageAdapter.setOnItemClickListener(this);
        mBinding.recommendAlbum.setAdapter(albumPageAdapter);
        mBinding.recommendAlbum.setInterval(3000);
        mBinding.recommendAlbum.startAutoScroll();

    }

    @Override
    public void setImageAds(List<Advertisement> advertisements)
    {
        AdvertisementImageAdapter mImageAdapter = new AdvertisementImageAdapter(advertisements,
                                                                                getActivity());
        mBinding.imageAdPage.setAdapter(mImageAdapter);
        mBinding.imageAdPage.setInterval(5000);
        mBinding.imageAdPage.startAutoScroll();


    }

    @OnClick(R.id.card_kge)
    public void onKaraoke()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(KGeCategoryFragment.class).putExtra("title", "K歌"));
        }
    }

    @OnClick(R.id.card_live)
    public void onShowLiveFragment()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(SatelliteLiveFragment.class).putExtra("title", "卫星直播"));
        }
    }

    @OnClick(R.id.card_movie)
    void onShowMovieList()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(MovieFragment.class).putExtra("title", "卫星影院"));
        }
    }

    @OnClick(R.id.card_bill)
    void onShowBill()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(BillFragment.class).putExtra("title", "消费账单"));
        }
    }

    @OnClick(R.id.card_WeiHuDong)
    void showWeiHuDong()
    {
        if (ViewUtils.isNotDoubleClick())
        {
            startFragment(new Request(BindWeiXinFragment.class).putExtra("title", "微互动"));
        }
    }


    @OnClick(
            {R.id.subButtonNewSong, R.id.subButtonWords, R.id.subButtonPinyin,
                    R.id.subButtonSinger, R.id.subButtonShouXie, R.id.subButtonCategory, R.id.subButtonHot,
                    R.id.subButtonLanguage})
    void showSongList(View view)
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            Request request = null;
            switch (view.getId())
            {
                case R.id.subButtonNewSong:
                    request = new Request(NewSongFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                          "新歌");
                    break;

                case R.id.subButtonHot:
                    request = new Request(SongListFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                           "热门歌曲");
                    break;
                case R.id.subButtonCategory:
                    request = new Request(SongCategoryFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                               "分类");
                    break;

                case R.id.subButtonLanguage:
                    request = new Request(SongListFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                           "语种");
                    break;

                case R.id.subButtonShouXie:
                    request = new Request(ShouXieFragmnet.class).putExtra(getString(R.string.fragmnet_name),
                                                                          "手写点歌");
                    break;

                case R.id.subButtonSinger:
                    request = new Request(SingerListFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                             "歌星");
                    break;

                case R.id.subButtonPinyin:
                    request = new Request(PinYinSongFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                             "拼音点歌");
                    break;

                case R.id.subButtonWords:
                    request = new Request(WordCountFragmnet.class)
                            .putExtra(getString(R.string.fragmnet_name), "字数点歌");
                    break;
            }

            if (request != null)
            {
                startFragment(request);
            }
        }
    }

    @Override
    public void onItemClickListener(Object object)
    {
        if (ViewUtils.isNotDoubleClick())
        {
            if (object instanceof Album)
            {
                startFragment(new Request(AlbumDetailFragmnet.class).putExtra(Album.class.getName(),
                                                                              (Album) object)
                                                                    .putExtra("title",
                                                                              ((Album) object).getTitle()));
            }
        }
    }
}
