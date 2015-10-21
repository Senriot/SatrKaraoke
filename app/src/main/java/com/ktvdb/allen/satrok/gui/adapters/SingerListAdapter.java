package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.SingerItemViewBinding;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.utils.ViewUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by Allen on 15/9/2.
 */
public class SingerListAdapter extends BaseAdapter<SingerListAdapter.ViewHolder, Singer>
{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                      R.layout.singer_item_view, null, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.onBind(getItem(position));
        holder.binding.mask.setOnClickListener(v -> {
            if (!ViewUtils.isFastDoubleClick())
            {
                if (getOnItemClickListener() != null)
                {
                    getOnItemClickListener().onItemClickListener(getItem(position));
                }
            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final SingerItemViewBinding binding;

        public ViewHolder(SingerItemViewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Singer singer)
        {
            binding.setSinger(singer);
            binding.executePendingBindings();
        }
    }
}
