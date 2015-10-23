package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.MediaPlayedItemBinding;
import com.ktvdb.allen.satrok.model.NewokMedia;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.utils.BindingUtils;
import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;

import java.util.List;

/**
 * Created by Allen on 15/10/21.
 */
public class PlayedPageAdapter  extends BaseSwipeAdapter<PlayedPageAdapter.ViewHolder>
{

    private List<NewokMedia> mData;
    MediaPlayer mPlayer;

    public PlayedPageAdapter()
    {
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        mData = mPlayer.getPlayedList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MediaPlayedItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                 R.layout.media_played_item,
                                                                 null,
                                                                 false);
        final ViewHolder holder = new ViewHolder(binding);
        com.malinskiy.superrecyclerview.swipe.SwipeLayout swipeLayout = holder.swipeLayout;
        swipeLayout.setDragEdge(com.malinskiy.superrecyclerview.swipe.SwipeLayout.DragEdge.Right);
        swipeLayout.setShowMode(com.malinskiy.superrecyclerview.swipe.SwipeLayout.ShowMode.PullOut);
        binding.btnReSelect.setOnClickListener(v -> {
            mItemManger.closeItem(holder.getLayoutPosition());
            mPlayer.addLastMedia(holder.getMedia());
        });
        binding.btnDelete.setOnClickListener(v -> {
            mItemManger.closeItem(holder.getLayoutPosition());
            mPlayer.delMedia(holder.getMedia());
        });
        binding.btnDetail.setOnClickListener(v -> BindingUtils.showSongDetail(v,
                                                                              (Song) holder.getMedia()));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        super.onBindViewHolder(viewHolder,position);
        NewokMedia media = getItem(position);
        viewHolder.onBind(media);
    }


    private NewokMedia getItem(int position)
    {
        return mData.get(position);
    }

    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    public static class ViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder
    {
        final MediaPlayedItemBinding binding;

        private NewokMedia media;

        public NewokMedia getMedia()
        {
            return media;
        }

        public ViewHolder(MediaPlayedItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(NewokMedia media)
        {
            this.media = media;
            binding.rootView
                    .setBackgroundResource(getLayoutPosition() % 2 == 0 ? R.drawable.song_list_item : R.drawable.song_list_item2);
            if (media != null)
            {
                binding.songName.setText(media.getName());
                binding.textNo.setText(getLayoutPosition() + 1 + ".");
                if (media instanceof Song)
                {
                    binding.singerName.setText(((Song) media).getSingerNames());
                }
            }
        }

    }
}
