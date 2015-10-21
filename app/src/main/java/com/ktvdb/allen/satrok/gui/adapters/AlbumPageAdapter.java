package com.ktvdb.allen.satrok.gui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.RecommendAlbumPageItemBinding;
import com.ktvdb.allen.satrok.model.Album;

import java.util.List;

/**
 * Created by Allen on 15/9/18.
 */
public class AlbumPageAdapter extends RecyclingPagerAdapter
{
    private final List<Album> albums;
    private final Context     context;

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener()
    {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public AlbumPageAdapter(List<Album> albums, Context context)
    {
        this.albums = albums;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup container)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            RecommendAlbumPageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                                                                                    context),
                                                                            R.layout.recommend_album_page_item,
                                                                            null,
                                                                            false);
            holder = new ViewHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.bind(albums.get(position));
        if (onItemClickListener != null)
        {
            holder.binding.getRoot()
                          .setOnClickListener(v -> onItemClickListener.onItemClickListener(albums.get(
                                  position)));
        }
        return convertView;
    }

    @Override
    public int getCount()
    {
        return albums.size();
    }

    private static class ViewHolder
    {
        private final RecommendAlbumPageItemBinding binding;

        private ViewHolder(RecommendAlbumPageItemBinding binding) {this.binding = binding;}

        public void bind(Album album)
        {
            binding.setAlbum(album);
        }
    }
}
