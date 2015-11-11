package com.ktvdb.allen.satrok.gui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSingerDetailBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.model.Album;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.presentation.view.SingerDetailView;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.app.AppObservable;


/**
 * Created by Allen on 15/9/4.
 */
@AutoInjector(MainActivity.class)
public class SingerDetailFragment extends LevelBaseFragment<FragmentSingerDetailBinding> implements SingerDetailView,
                                                                                                    RadioGroup.OnCheckedChangeListener
{
    Singer mSinger;
    @Inject
    RestService mService;
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
        mSinger = (Singer) getRequest().getSerializableExtra("singer");

    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_singer_detail;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        mBinding.singerInfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        AppObservable.bindFragment(this, mService.getSinger(mSinger.getId()))
                     .onErrorResumeNext(Observable.<Singer>empty())
                     .subscribe(mBinding::setSinger);

        mHandler.postDelayed(this::init, 150);
    }



    private void init()
    {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, SingerTabSongsFragment.newInstance(mSinger));
        transaction.commit();
    }

    @Override
    public void onItemClickListener(Object object)
    {
        if (ViewUtils.isNotDoubleClick())
        {
            Album album = (Album) object;
            startFragment(new Request(AlbumDetailFragmnet.class).putExtra(Album.class.getName(),
                                                                          album)
                                                                .putExtra("title",
                                                                          album.getTitle()));
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_album)
        {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.content, SingerTabAlbumsFragment.newInstance(mSinger, this));
            transaction.commit();
        }
        else
        {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.content, SingerTabSongsFragment.newInstance(mSinger));
            transaction.commit();
        }
    }
}
