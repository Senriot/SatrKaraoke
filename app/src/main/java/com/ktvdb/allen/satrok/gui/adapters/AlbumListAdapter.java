package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.AlbumGridItemBinding;
import com.ktvdb.allen.satrok.model.Album;

/**
 * Created by Allen on 15/7/14.
 */
public class AlbumListAdapter extends BaseAdapter<AlbumListAdapter.ViewHolder, Album>
{


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        AlbumGridItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                               R.layout.album_grid_item,
                                                               null,
                                                               false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bind(getItem(position));
        holder.binding.mask.setOnClickListener(v -> {
            if (onItemClickListener != null)
            {
                onItemClickListener.onItemClickListener(getItem(position));
            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final AlbumGridItemBinding binding;

        public ViewHolder(AlbumGridItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Album item)
        {
            binding.setAlbum(item);
        }
    }
}
