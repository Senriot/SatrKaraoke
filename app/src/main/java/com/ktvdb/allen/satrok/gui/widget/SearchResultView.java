package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.SearchPopupViewBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.adapters.SearchResultAdapter;
import com.ktvdb.allen.satrok.model.FullSearchResult;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.service.RestService;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.apache.commons.lang3.StringUtils;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/9/13.
 */
public class SearchResultView extends FrameLayout
{
    SearchPopupViewBinding mBinding;

    SearchResultAdapter mAdapter;

    RestService mService;

    GridLayoutManager layoutManager;

    public SearchResultView(Context context)
    {
        this(context, null);
    }

    public SearchResultView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SearchResultView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                           R.layout.search_popup_view,
                                           this,
                                           true);
        mService = StarokApplication.getAppContext().getComponent().restService();
    }

    public void onReload(String text)
    {

        if (StringUtils.isBlank(text))
        {
            stupListView(null);
        }
        else
        {
            mService.fullSearch(text).observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(Observable.<FullSearchResult>empty())
                    .subscribe(fullSearchResult -> {

                        if (!fullSearchResult.getSongs().isEmpty())
                        {
                            FullSearchResult.ContentItem<Song> item = new FullSearchResult.ContentItem<Song>();
                            item.setTitle("歌曲");
                            item.setList(fullSearchResult.getSongs());
                            fullSearchResult.addItem(item);
                        }

                        if (!fullSearchResult.getSingers().isEmpty())
                        {
                            FullSearchResult.ContentItem<Singer> item = new FullSearchResult.ContentItem<Singer>();
                            item.setTitle("歌星");
                            item.setList(fullSearchResult.getSingers());
                            fullSearchResult.addItem(item);
                        }
                        stupListView(fullSearchResult);
                    });
        }
    }

    @UiThread
    private void stupListView(FullSearchResult result)
    {

        this.post(() -> {
            if (mAdapter == null)
            {
                mAdapter = new SearchResultAdapter(result);
            }
            else
            {
                mAdapter.setSearchResult(result);
            }
            mBinding.listView.setAdapter(mAdapter);
            if (layoutManager == null)
            {
                layoutManager = new GridLayoutManager(getContext(), 1);
                SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter,
                                                                             layoutManager);
                layoutManager.setSpanSizeLookup(lookup);
                mBinding.listView.setLayoutManager(layoutManager);
            }
            mAdapter.notifyDataSetChanged();
        });

    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }
}
