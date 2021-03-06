package com.ktvdb.allen.satrok.gui;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.fragmentmaster.app.IMasterFragment;
import com.fragmentmaster.app.MasterActivity;
import com.fragmentmaster.app.Request;
import com.google.android.exoplayer.ExoPlayer;
import com.ktvdb.allen.satrok.DaggerScope;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.ActivityMainBinding;
import com.ktvdb.allen.satrok.event.PlayWhenReadyEvent;
import com.ktvdb.allen.satrok.event.SearchInputEvent;
import com.ktvdb.allen.satrok.event.SearchSingerItemClickEvent;
import com.ktvdb.allen.satrok.gui.fragment.MainFragment;
import com.ktvdb.allen.satrok.gui.fragment.SelectedFragment;
import com.ktvdb.allen.satrok.gui.fragment.SingerDetailFragment;
import com.ktvdb.allen.satrok.gui.widget.KeyBoardDialog;
import com.ktvdb.allen.satrok.gui.widget.SearchResultView;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.model.FullSearchResult;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.presentation.MainPresentation;
import com.ktvdb.allen.satrok.presentation.view.MainView;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.PlayCenterService;
import com.ktvdb.allen.satrok.service.PlayServiceHelper;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


@AutoComponent(
        modules = {MainActivity.Module.class},
        dependencies = StarokApplication.class
)
@AutoInjector
@DaggerScope(MainActivity.class)
public class MainActivity extends MasterActivity implements MainView,
                                                            PlayCenterService.Client.Callback,
                                                            SeekBar.OnSeekBarChangeListener,
                                                            KeyBoardDialog.KeyboardListener
{
    @Inject
    WindowManager mWindowManager;

    @Inject
    MediaPlayer mPlayer;

    @Inject
    RestService mService;

    MainPresentation    mPresentation;
    ActivityMainBinding mBinding;
    private static MainActivityComponent component;
    private PlayServiceHelper mHelper = new PlayServiceHelper(this, this);

    private PopupWindow searchPopup;
    @Inject
    ConfigManager configManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHelper.onStart();
        EventBus.getDefault().register(this);
        component = DaggerMainActivityComponent.builder()
                                               .starokApplicationComponent(StarokApplication.getAppContext()
                                                                                            .getComponent())
                                               .module(new Module())
                                               .build();
        component.inject(this);

        mPresentation = new MainPresentation(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setP(mPresentation);
        mBinding.setModel(StarokApplication.getAppContext()
                                           .getComponent()
                                           .playHubModel());
        ButterKnife.bind(this);
        final EditText searchView = mBinding.topBar.searchView;
        searchView.setOnClickListener(v -> onShowKeyboard());
        searchView.setInputType(InputType.TYPE_NULL);
        mBinding.bottomBar.playerSeekbar.setOnSeekBarChangeListener(this);
        mBinding.topBar.selectedView.setOnClickListener(v -> {

            try
            {
                if (ViewUtils.isNotDoubleClick())
                {
                    if (getFragmentMaster().getPrimaryFragment() instanceof SelectedFragment)
                    {
                        return;
                    }
                    getFragmentMaster().getPrimaryFragment().startFragment(new Request(
                            SelectedFragment.class).putExtra("title", "??????"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        });

        getFragmentMaster().install(R.id.main_container,
                                    new Request(MainFragment.class).putExtra("title", "??????"),
                                    true);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public static MainActivityComponent getComponent()
    {
        return component;
    }

    @Override
    public void onConnected(PlayCenterService service)
    {
        mPresentation.setService(service);
    }

    @Override
    public void onDisconnected()
    {
    }

    @Subscriber
    void onSearchTextChanged(SearchInputEvent event)
    {
        mBinding.topBar.searchView.setText(event.keyword);
    }

    @Subscriber
    void onSearchSingerItenClick(SearchSingerItemClickEvent event)
    {
        if (ViewUtils.isNotDoubleClick())
        {
            IMasterFragment fragment = getFragmentMaster().getPrimaryFragment();
            if (fragment instanceof SingerDetailFragment)
            {
                getFragmentMaster().finishFragment(fragment, 0, null);
            }
            getFragmentMaster().getPrimaryFragment()
                               .startFragment(new Request(SingerDetailFragment.class)
                                                      .putExtra("singer", event.singer)
                                                      .putExtra("title",
                                                                event.singer.getSimpName()));

            searchPopup.dismiss();
        }
    }

    @Subscriber
    void onPlayWhenReady(PlayWhenReadyEvent event)
    {
        if (event.playWhenReady && event.playbackState == ExoPlayer.STATE_READY)
        {
            if (mPlayer.getMedia() instanceof Song)
            {
                String imageUrl = configManager.getSongImageUrl(mPlayer.getMedia()
                                                                       .getId() + "/200/200");
                mBinding.bottomBar.singerAvatar.setImageURI(Uri.parse(imageUrl));
            }
            else
            {
                mBinding.bottomBar.singerAvatar.setImageURI(Uri.parse(
                        "android.resource://" + StarokApplication.getAppContext().getPackageName() +
                                "/" + R.drawable.singer_def));
            }
        }
    }

    synchronized void onShowKeyboard()
    {

        KeyBoardDialog.showKeyboard(this, this, mBinding.topBar.searchView.getText().toString());
//        KeyBoard.showKeyboard(this, this, mBinding.topBar.searchView.getText().toString());

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser)
        {
            mPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onTextChanged(String text)
    {
        String mText = mBinding.topBar.searchView.getText().toString();
        if (!mText.equals(text))
        {
            mBinding.topBar.searchView.setText(text);
            AppObservable.bindActivity(this, mService.fullSearch(text))
                         .onErrorResumeNext(Observable.<FullSearchResult>empty())
                         .map(fullSearchResult -> {
                             if (text.equals(fullSearchResult.getSearchText()))
                             {
                                 if (!fullSearchResult.getSongs().isEmpty())
                                 {
                                     FullSearchResult.ContentItem<Song> item = new FullSearchResult.ContentItem<>();
                                     item.setTitle("??????");
                                     item.setList(fullSearchResult.getSongs());
                                     fullSearchResult.addItem(item);
                                 }

                                 if (!fullSearchResult.getSingers().isEmpty())
                                 {
                                     FullSearchResult.ContentItem<Singer> item = new FullSearchResult.ContentItem<>();
                                     item.setTitle("??????");
                                     item.setList(fullSearchResult.getSingers());
                                     fullSearchResult.addItem(item);
                                 }
                                 return fullSearchResult;
                             }
                             return null;
                         })
                         .subscribe(this::showSearchView);
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
    public void setTextAds(List<Advertisement> advertisements)
    {
        mBinding.topBar.marqueeView.addScrollText(advertisements);
    }

    @Override
    public void showBitmap(Bitmap bitmap)
    {
//        mBinding.imageView4.setImageBitmap(bitmap);
    }

    @Override
    public void setMute(boolean b)
    {
        mBinding.bottomBar.ctrlJingYin.setIconBackground(b ? R.drawable.ic_volume_pressed_background : R.drawable.ic_popbutton_background);
        mBinding.bottomBar.ctrlJingYin.setIconRes(b ? R.drawable.ic_jingyin_baochi : R.drawable.ic_jingyin);
    }

    @Override
    public void showSearchView(FullSearchResult fullSearchResult)
    {
        if (fullSearchResult == null) return;
        if (searchPopup == null)
        {
            SearchResultView rootView = new SearchResultView(this);
            searchPopup = new PopupWindow(this);
            searchPopup.setFocusable(true);
            searchPopup.setWidth(mBinding.topBar.searchBar.getWidth());
            searchPopup.setHeight(800);
            searchPopup.setContentView(rootView);
            searchPopup.setBackgroundDrawable(new BitmapDrawable());
            searchPopup.update();
            searchPopup.setOnDismissListener(() -> mBinding.topBar.searchView.setText(""));
        }

        if (!searchPopup.isShowing())
        {
            searchPopup.showAsDropDown(mBinding.topBar.searchBar);
        }
        SearchResultView rootView = (SearchResultView) searchPopup.getContentView();
        rootView.setData(fullSearchResult);
    }


    @dagger.Module
    public static class Module
    {

    }

    float x_temp01 = 0.0f;
    float y_temp01 = 0.0f;
    float x_temp02 = 0.0f;
    float y_temp02 = 0.0f;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev)
    {
        float x = ev.getX();
        float y = ev.getY();


        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                x_temp01 = x;
                y_temp01 = y;
            }
            break;
            case MotionEvent.ACTION_UP:
            {
                getFragmentMaster().getPrimaryFragment().setSlideable(true);
            }
            break;
            case MotionEvent.ACTION_MOVE:
            {
                x_temp02 = x;
                y_temp02 = y;

                if (x < 729)
                {
                    getFragmentMaster().getPrimaryFragment().setSlideable(true);
                }
                else
                {
                    getFragmentMaster().getPrimaryFragment().setSlideable(false);
                }
            }
            break;

        }

        try
        {
            return super.dispatchTouchEvent(ev);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
