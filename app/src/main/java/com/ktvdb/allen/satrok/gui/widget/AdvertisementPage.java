package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.badoo.mobile.util.WeakHandler;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.ImageadPageItemBinding;
import com.ktvdb.allen.satrok.model.Advertisement;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Allen on 15/10/27.
 */
public class AdvertisementPage extends FrameLayout
{
    RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;
    private AdAdapter         mAdapter;

    private WeakHandler mHandler;

    private int currentPos = 0;

    public AdvertisementPage(Context context)
    {
        this(context, null);
    }

    public AdvertisementPage(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public AdvertisementPage(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setOnTouchListener((v, event) -> true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                   ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, params);
        mLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setAdvertisements(List<Advertisement> ads)
    {
        mAdapter = new AdAdapter(ads);
        mRecyclerView.setAdapter(mAdapter);
        mHandler = new WeakHandler();
        mHandler.postDelayed(scrollRunnable, 5000);
    }

    private Runnable scrollRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            currentPos += 1;
            if (currentPos >= mAdapter.getItemCount() - 1)
            {
                currentPos = 0;
            }

            mLayoutManager.scrollToPosition(currentPos);
            mHandler.postDelayed(scrollRunnable, 5000);
        }
    };


    public static class AdAdapter extends RecyclerView.Adapter<AdAdapter.ViewHolder>
    {

        private List<Advertisement> data;

        public AdAdapter(List<Advertisement> data)
        {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ImageView imageView = new ImageView(parent.getContext());
            return new ViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            holder.onBind(data.get(position));
        }

        @Override
        public int getItemCount()
        {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {

            public ViewHolder(View itemView)
            {
                super(itemView);
            }


            public void onBind(Advertisement a)
            {
                ConfigManager config = StarokApplication.getAppContext()
                                                        .getComponent()
                                                        .configManager();
                String url = config.getAdvertisementUrl(a.getFileName());
                Picasso.with(itemView.getContext())
                       .load(Uri.parse(url))
                       .placeholder(R.drawable.ad_729_729)
                       .into((ImageView) itemView);
            }

        }
    }
}
