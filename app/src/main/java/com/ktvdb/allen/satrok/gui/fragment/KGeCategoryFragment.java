package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.badoo.mobile.util.WeakHandler;
import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentKgeCategoryBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.AlbumPageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.presentation.KGeCategoryPresentation;
import com.ktvdb.allen.satrok.presentation.view.KeGeCategoryView;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@FragmnetTitle("K歌")
@AutoInjector(MainActivity.class)
public class KGeCategoryFragment extends LevelBaseFragment<FragmentKgeCategoryBinding> implements KeGeCategoryView,
                                                                                                  OnItemClickListener
{
    KGeCategoryPresentation mPresentation;

    private AlbumPageAdapter albumPageAdapter;

    private WeakHandler mHandler;

    @Inject
    RestService mService;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_kge_category;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
        mHandler = new WeakHandler();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mHandler.postDelayed(() -> mService.getRecommendAlbums().observeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .onErrorResumeNext(Observable.<PageResponse<Album>>empty())
                                           .subscribe(albumPageResponse -> {
                                               startScrollAlbum(albumPageResponse.getContent());
                                           }), 300);
    }

    @OnClick(R.id.cardNewSong)
    void showNewSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(NewSongFragment.class));
        }
    }

    @OnClick(R.id.cardSinger)
    void showSingerList()
    {
        if (ViewUtils.isNotDoubleClick())
        {
            startFragment(new Request(SingerListFragment.class));
        }
    }

    @OnClick(R.id.cardHotSong)
    void showHotSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(HotSongListFragment.class));
        }
    }

    @OnClick(R.id.cardPinYin)
    void showPinYinSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(PinYinSongFragment.class));
        }
    }

    @OnClick(R.id.cardShowXie)
    void onShowShowXie()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(ShouXieFragmnet.class));
        }
    }

    @OnClick(R.id.cardLanguage)
    void onLanguageList()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(SongListLanguageFragmnet.class));
        }
    }

    @OnClick(R.id.cardWordCount)
    void showWordCount()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(WordCountFragmnet.class));
        }
    }

    @OnClick(R.id.cardType)
    void showCategory()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(SongCategoryFragment.class));
        }
    }

    @Override
    public void startScrollAlbum(List<Album> albums)
    {
        albumPageAdapter = new AlbumPageAdapter(albums, getActivity());
        albumPageAdapter.setOnItemClickListener(this);
        mBinding.albumPage.setAdapter(albumPageAdapter);
        mBinding.albumPage.setInterval(3000);
        mBinding.albumPage.startAutoScroll();
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
