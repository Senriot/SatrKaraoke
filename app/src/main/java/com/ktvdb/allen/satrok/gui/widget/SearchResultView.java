package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.SearchPopupViewBinding;
import com.ktvdb.allen.satrok.event.PlayQueueChengedEvent;
import com.ktvdb.allen.satrok.gui.adapters.SearchResultAdapter;
import com.ktvdb.allen.satrok.model.FullSearchResult;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Created by Allen on 15/9/13.
 */
public class SearchResultView extends FrameLayout
{
    SearchPopupViewBinding mBinding;

    SearchResultAdapter mAdapter;


    GridLayoutManager mLayoutManager;


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
    }

    public void setData(FullSearchResult result)
    {
        if (mAdapter == null)
        {
            mAdapter = new SearchResultAdapter(result);
            mLayoutManager = new GridLayoutManager(getContext(), 1);
            SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mAdapter,
                                                                         mLayoutManager);
            mLayoutManager.setSpanSizeLookup(lookup);
            mBinding.listView.setLayoutManager(mLayoutManager);
            mBinding.listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        else
        {
            mAdapter.setSearchResult(result);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Subscriber
    void onPlayListChanged(PlayQueueChengedEvent event)
    {
        mAdapter.notifyDataSetChanged();
    }
}
