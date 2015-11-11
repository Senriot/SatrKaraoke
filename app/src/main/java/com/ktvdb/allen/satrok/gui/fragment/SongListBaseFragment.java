package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.badoo.mobile.util.WeakHandler;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSongListPageBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.widget.AdPageView;
import com.ktvdb.allen.satrok.gui.widget.KeyBoardDialog;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.BindingUtils;
import com.ktvdb.allen.satrok.utils.ConfigManager;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.OnClick;
import rx.Observable;
import rx.android.app.AppObservable;

/**
 * A simple {@link Fragment} subclass.
 */

@AutoInjector(MainActivity.class)
public abstract class SongListBaseFragment extends LevelBaseFragment<FragmentSongListPageBinding> implements RadioGroup.OnCheckedChangeListener,
                                                                                                             AdPageView.OnScrollListener,
                                                                                                             KeyBoardDialog.KeyboardListener
{

    @Inject
    RestService mService;

    @Inject
    ConfigManager configManager;

    WeakHandler mHandler = new WeakHandler();

    SongListPageFragment currentFragment;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_song_list_page;
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
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        onLoadTabView();
        mHandler.postDelayed(() -> {
            init();
            getImageAds();
        }, 200);
    }

    protected abstract void init();

    private void getImageAds()
    {
        AppObservable.bindFragment(this,
                                   mService.geLeftImageAds(configManager.getRoomInfo()
                                                                        .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(mBinding.adPageView::setAdvertisements);
    }

    protected abstract void onLoadTabView();

    public void replacePage(SongQueryCondition condition)
    {
        currentFragment = SongListPageFragment.newInstance(condition);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, currentFragment);
        transaction.commit();
    }


    @Override
    public abstract void onCheckedChanged(RadioGroup group, int checkedId);

    @OnClick(R.id.btnKeyboard)
    public void showKeyboard()
    {
        if (currentFragment != null)
        {
            KeyBoardDialog.showKeyboard(getActivity(),
                                        this,
                                        currentFragment.getCondition().getKeyword());
        }
    }

    @Override
    public void onTextChanged(String text)
    {
        if (currentFragment != null)
        {
            currentFragment.onSearch(text);
        }
    }

    @Override
    public void onShowed()
    {
        mBinding.btnKeyboard.setImageResource(R.drawable.keyboard_button_close);
    }

    @Override
    public void onDismiss()
    {
        mBinding.btnKeyboard.setImageResource(R.drawable.keyboard_button_open);
    }

    protected void onLoadLanguage()
    {
        AppObservable.bindFragment(this, mService.getLanguages())
                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                     .subscribe(sysDictionaries -> {
                         int id = 100;
                         for (SysDictionary tab : sysDictionaries)
                         {
                             RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                                  .inflate(R.layout.tab_radiobutton,
                                                                                           null);
                             radioButton.setText(tab.getDictName());
                             radioButton.setId(id++);
                             radioButton.setTag(tab.getId());
                             mBinding.categoryRadioGroup.addView(radioButton);
                         }
                     });
    }

    @Override
    public void onScroll(String url)
    {
        BindingUtils.stackBlurImage(mBinding.background, url);
    }
}
