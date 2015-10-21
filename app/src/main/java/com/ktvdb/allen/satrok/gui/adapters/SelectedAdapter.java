package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ktvdb.allen.satrok.Config;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.MediaSelectedItemBinding;
import com.ktvdb.allen.satrok.databinding.SongInfoPanelBinding;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.NewokMedia;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.swipe.SwipeLayout;
import com.ktvdb.allen.satrok.swipe.adapters.RecyclerSwipeAdapter;
import com.ktvdb.allen.satrok.utils.BindingUtils;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/7/19.
 */
public class SelectedAdapter extends RecyclerSwipeAdapter<SelectedAdapter.ViewHolder>
{

    MediaPlayer mPlayer;
    private boolean played;

    private List<NewokMedia> list;

    public SelectedAdapter()
    {
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        setList(mPlayer.getPlayList());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MediaSelectedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                   R.layout.media_selected_item,
                                                                   null,
                                                                   false);

        final ViewHolder holder = new ViewHolder(binding);
        binding.recyclerviewSwipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        binding.btnTop.setOnClickListener(v -> {
            mItemManger.closeItem(holder.getLayoutPosition());
            mItemManger.bind(holder.itemView, holder.getLayoutPosition());
            mPlayer.AddFistMedia(holder.getMedia());
        });
        binding.btnDelete.setOnClickListener(v -> {
            mItemManger.closeItem(holder.getLayoutPosition());
            mItemManger.bind(holder.itemView, holder.getLayoutPosition());
            mItemManger.closeAllExcept(binding.recyclerviewSwipe);
            mPlayer.delMedia(holder.getMedia());
        });
        binding.btnReSelect.setOnClickListener(v -> {
            mItemManger.closeItem(holder.getLayoutPosition());
            mItemManger.bind(holder.itemView, holder.getLayoutPosition());
            mPlayer.addLastMedia(holder.getMedia());
        });
        binding.btnDetail.setOnClickListener(v -> BindingUtils.showSongDetail(v,
                                                                              (Song) holder.getMedia()));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        NewokMedia media = getItem(position);
        holder.onBind(media);
        mItemManger.bind(holder.itemView, position);
    }

    private NewokMedia getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public int getItemViewType(int position)
    {
        NewokMedia item = getItem(position);
        if (item instanceof Song)
        {
            return Config.MEDIA_TYPE_SONG;
        }
        if (item instanceof Movie)
        {
            return Config.MEDIA_TYPE_MOVIE;
        }
        if (item instanceof LiveProgram)
        {
            return Config.MEDIA_TYPE_LIVE;
        }

        return -1;
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setPlayed(boolean played)
    {
        this.played = played;
    }

    public boolean isPlayed()
    {
        return played;
    }

    @Override
    public int getSwipeLayoutResourceId(int position)
    {
        return R.id.recyclerview_swipe;
    }

    public synchronized void setList(List<NewokMedia> playedList)
    {
        list = playedList;
        notifyDatasetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        final MediaSelectedItemBinding binding;

        private NewokMedia media;

        public NewokMedia getMedia()
        {
            return media;
        }

        public ViewHolder(MediaSelectedItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(NewokMedia media)
        {
            this.media = media;
            binding.getRoot()
                   .setBackgroundResource(getLayoutPosition() % 2 == 0 ? R.drawable.song_list_item : R.drawable.song_list_item2);
            if (media != null)
            {
                binding.songName.setText(media.getName());
                binding.textNo.setText(getLayoutPosition() + 1 + ".");
                if (media instanceof Song)
                {
                    binding.singerName.setText(((Song) media).getSingerNames());
                }
                binding.btnTop.setVisibility(getLayoutPosition() == 0 ? View.GONE : View.VISIBLE);
                binding.btnReSelect.setVisibility(played ? View.VISIBLE : View.GONE);
            }
        }

    }
}
