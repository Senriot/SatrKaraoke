package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentSingerListBinding;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.OnItemClickListener;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.gui.widget.KeyBoardDialog;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.SingerQueryCondition;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.service.RestService;
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
@FragmnetTitle("歌星点歌")
@AutoInjector(MainActivity.class)
public class SingerListFragment extends LevelBaseFragment<FragmentSingerListBinding> implements RadioGroup.OnCheckedChangeListener,
                                                                                                OnItemClickListener,
                                                                                                KeyBoardDialog.KeyboardListener
{

    @Inject
    RestService mService;

    @Inject
    ConfigManager configManager;

    Handler mHandler = new Handler();

    SingerListPageFragment currentFragment;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_singer_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);
    }


    private void getImageAds()
    {
        AppObservable.bindFragment(this,
                                   mService.geLeftImageAds(configManager.getRoomInfo()
                                                                        .getCode()))
                     .onErrorResumeNext(Observable.<List<Advertisement>>empty())
                     .subscribe(mBinding.adImagePage::setAdvertisements);
    }

    private void init()
    {
        AppObservable.bindFragment(this, mService.getSingerType())
                     .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                     .subscribe(this::updateTabView);

        replacePage(new SingerQueryCondition(null, "hot", Direction.DESC,
                                             SingerQueryCondition.SingerCategory.None));
    }

    private void updateTabView(List<SysDictionary> sysDictionaries)
    {
        int id = 100;
        for (SysDictionary type : sysDictionaries)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(type.getDictName());
            radioButton.setTag(type.getId());
            radioButton.setId(id++);
            mBinding.categoryRadioGroup.addView(radioButton);

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        mHandler.postDelayed(() -> {
            init();
            getImageAds();
        }, 150);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        RadioButton button = (RadioButton) group.findViewById(checkedId);
        String      type   = (String) button.getTag();
        if (checkedId == R.id.rb_all)
        {
            replacePage(new SingerQueryCondition(null, "hot", Direction.DESC,
                                                 SingerQueryCondition.SingerCategory.None));
        }
        else
        {
            replacePage(new SingerQueryCondition(type, "hot", Direction.DESC,
                                                 SingerQueryCondition.SingerCategory.SingerType));
        }
    }

    public void replacePage(SingerQueryCondition condition)
    {
        currentFragment = SingerListPageFragment.newInstance(condition, this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, currentFragment);
        transaction.commit();
    }

    @OnClick(R.id.btnKeyboard)
    public void showKeyboard()
    {
        KeyBoardDialog.showKeyboard(getActivity(),
                                    this,
                                    currentFragment.getCondition().getKeyword());
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

    }

    @Override
    public void onDismiss()
    {

    }

    @Override
    public void onItemClickListener(Object object)
    {
        Singer singer = (Singer) object;
        startFragment(new Request(SingerDetailFragment.class)
                              .putExtra("singer", singer)
                              .putExtra("title", singer.getSimpName()));
    }
}
