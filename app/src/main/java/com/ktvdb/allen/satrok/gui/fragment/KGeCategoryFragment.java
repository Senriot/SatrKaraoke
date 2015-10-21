package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentKgeCategoryBinding;
import com.ktvdb.allen.satrok.gui.adapters.AlbumPageAdapter;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.presentation.KGeCategoryPresentation;
import com.ktvdb.allen.satrok.presentation.view.KeGeCategoryView;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import java.util.List;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class KGeCategoryFragment extends LevelBaseFragment<FragmentKgeCategoryBinding> implements KeGeCategoryView,
                                                                                                  OnItemClickListener
{
    KGeCategoryPresentation mPresentation;

    private AlbumPageAdapter albumPageAdapter;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_kge_category;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mPresentation = new KGeCategoryPresentation(this);
    }

    @OnClick(R.id.cardNewSong)
    void showNewSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(NewSongFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                      "新歌"));
        }
    }

    @OnClick(R.id.cardSinger)
    void showSingerList()
    {
        if (ViewUtils.isNotDoubleClick())
        {
            startFragment(new Request(SingerListFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                         "歌星"));
        }
    }

    @OnClick(R.id.cardHotSong)
    void showHotSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(NewSongFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                      "热门歌曲"));
        }
    }

    @OnClick(R.id.cardPinYin)
    void showPinYinSong()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(PinYinSongFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                         "拼音点歌"));
        }
    }

//    @OnClick(R.id.card_bihua)
//    void showBiHua()
//    {
//        if (!ViewUtils.isFastDoubleClick())
//        {
//            startFragment(new Request(BiHauSongFragment.class).putExtra(getString(R.string.fragmnet_name),
//                                                                        "笔画"));
//        }
//    }

    @OnClick(R.id.cardShowXie)
    void onShowShowXie()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(ShouXieFragmnet.class).putExtra(getString(R.string.fragmnet_name),
                                                                      "手写"));
        }
    }

    @OnClick(R.id.cardLanguage)
    void onLanguageList()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(SongListFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                       "语种"));
        }
    }

    @OnClick(R.id.cardWordCount)
    void showWordCount()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(WordCountFragmnet.class).putExtra(getString(R.string.fragmnet_name),
                                                                        "字数"));
        }
    }

    @OnClick(R.id.cardType)
    void showCategory()
    {
        if (!ViewUtils.isFastDoubleClick())
        {
            startFragment(new Request(SongCategoryFragment.class).putExtra(getString(R.string.fragmnet_name),
                                                                           "分类"));
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
