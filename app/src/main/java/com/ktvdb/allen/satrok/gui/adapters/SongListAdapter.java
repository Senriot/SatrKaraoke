package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.SongInfoPanelBinding;
import com.ktvdb.allen.satrok.databinding.SongListItemViewBinding;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.service.RestService;
import com.ktvdb.allen.satrok.swipe.SwipeLayout;
import com.ktvdb.allen.satrok.swipe.adapters.RecyclerSwipeAdapter;
import com.ktvdb.allen.satrok.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Allen on 15/8/30.
 */
public class SongListAdapter extends RecyclerSwipeAdapter<SongListAdapter.ViewHolder>
{

    private List<Song> list = new ArrayList<>();

    private MediaPlayer mPlayer;

    public SongListAdapter()
    {
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
        setMode(Attributes.Mode.Single);
    }

    @Override
    public int getSwipeLayoutResourceId(int position)
    {
        return R.id.swipe;
    }

    public enum SongItemType
    {
        TYPE_NONE,
        TYPE_SELECTED,
        TYPE_TOP,
        TYPE_PLAYING
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SongListItemViewBinding binding = DataBindingUtil.inflate(inflater,
                                                                        R.layout.song_list_item_view,
                                                                        parent,
                                                                        false);

        final ViewHolder viewHolder = new ViewHolder(binding);
        binding.swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        viewHolder.binding.rootView.setOnClickListener(v -> mPlayer.addLastMedia(getItem(viewHolder.getLayoutPosition())));
        viewHolder.binding.btnDelete.setOnClickListener(v -> {
            mItemManger.closeItem(viewHolder.getLayoutPosition());
            mPlayer.delMedia(list.get(viewHolder.getLayoutPosition()));
        });
        viewHolder.binding.btnTop.setOnClickListener(v -> {
            mItemManger.closeItem(viewHolder.getLayoutPosition());
            mPlayer.AddFistMedia(list.get(viewHolder.getLayoutPosition()));
        });
        viewHolder.binding.btnDetail.setOnClickListener(v -> {
            final Song model = getItem(viewHolder.getLayoutPosition());
            final SongInfoPanelBinding infoPanelBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(v.getContext()),
                    R.layout.song_info_panel,
                    null,
                    false);
            infoPanelBinding.setSong(model);
            int width = View.MeasureSpec.makeMeasureSpec(0,
                                                         View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0,
                                                          View.MeasureSpec.UNSPECIFIED);
            infoPanelBinding.getRoot().measure(width, height);
            PopupWindow pw = new PopupWindow(v.getContext());
            pw.setFocusable(true);
            pw.setWidth(infoPanelBinding.getRoot().getMeasuredWidth());
            pw.setHeight(infoPanelBinding.getRoot().getMeasuredHeight());
            pw.setContentView(infoPanelBinding.getRoot());
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.update();
            pw.showAsDropDown(v, -v.getWidth(), -v.getHeight(), Gravity.RIGHT);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final Song s = list.get(position);
        holder.onBind(s);
        switch (getItemType(s))
        {
            case TYPE_NONE:
                holder.binding.textFlag.setVisibility(View.INVISIBLE);
                holder.binding.btnDelete.setVisibility(View.GONE);
                holder.binding.btnTop.setVisibility(View.VISIBLE);
                holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                    .getColor(R.color.song_list_item_selecded_text_color));
                break;
            case TYPE_SELECTED:
                holder.binding.textFlag.setVisibility(View.VISIBLE);
                holder.binding.textFlag.setText("--预约 " + String.format("%03d", mPlayer.selectIndex(
                        s) + 1));
                holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                    .getColor(R.color.song_list_item_selecded_text_color));
                holder.binding.btnTop.setVisibility(View.VISIBLE);
                holder.binding.btnDelete.setVisibility(View.VISIBLE);
                break;
            case TYPE_TOP:
                holder.binding.textFlag.setVisibility(View.VISIBLE);
                holder.binding.textFlag.setText("--下首播放 ");
                holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                    .getColor(R.color.song_list_item_selecded_text_color));
                holder.binding.btnTop.setVisibility(View.GONE);
                holder.binding.btnDelete.setVisibility(View.VISIBLE);
                break;
            case TYPE_PLAYING:
                holder.binding.textFlag.setVisibility(View.VISIBLE);
                holder.binding.textFlag.setText("--正播放");
                holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                    .getColor(R.color.song_list_item_play_text_color));
                holder.binding.btnTop.setVisibility(View.GONE);
                holder.binding.btnDelete.setVisibility(View.GONE);
                break;
        }
        mItemManger.bind(holder.itemView, position);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void add(Collection<Song> songs)
    {
        this.list.addAll(songs);
        notifyDataSetChanged();
    }

    public void clear()
    {
        list.clear();
        notifyDataSetChanged();
    }

    public Song getItem(int position)
    {
        return list.get(position);
    }

    private SongItemType getItemType(Song song)
    {
        if (song.equals(mPlayer.getMedia()))
        {
            return SongItemType.TYPE_PLAYING;
        }
        if (mPlayer.isSelected(song))
        {
            if (mPlayer.selectIndex(song) == 0)
            {
                return SongItemType.TYPE_TOP;
            }
            return SongItemType.TYPE_SELECTED;
        }
        return SongItemType.TYPE_NONE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final SongListItemViewBinding binding;

        public ViewHolder(SongListItemViewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Song song)
        {
            song.setEven(getPosition() % 2 == 0);
            binding.setSong(song);
            binding.executePendingBindings();
        }
    }


}
