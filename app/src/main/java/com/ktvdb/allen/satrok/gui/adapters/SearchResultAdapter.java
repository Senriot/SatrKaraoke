package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.SearchPopupHeaderBinding;
import com.ktvdb.allen.satrok.databinding.SearchPopupItemBinding;
import com.ktvdb.allen.satrok.event.SearchSingerItemClickEvent;
import com.ktvdb.allen.satrok.model.FullSearchResult;
import com.ktvdb.allen.satrok.model.Singer;
import com.ktvdb.allen.satrok.model.Song;
import com.ktvdb.allen.satrok.service.MediaPlayer;
import com.ktvdb.allen.satrok.swipe.SwipeLayout;
import com.ktvdb.allen.satrok.utils.BindingUtils;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import org.simple.eventbus.EventBus;

/**
 * Created by Allen on 15/9/13.
 */
public class SearchResultAdapter extends SectionedRecyclerViewAdapter<SearchResultAdapter.HeaderViewHolder,
        SearchResultAdapter.ItemViewHolder, SearchResultAdapter.FooterViewHolder>
{

    private FullSearchResult searchResult = new FullSearchResult();

    private MediaPlayer mPlayer;


    public FullSearchResult getSearchResult()
    {
        return searchResult;
    }

    public void setSearchResult(FullSearchResult searchResult)
    {
        this.searchResult = searchResult;
    }

    public SearchResultAdapter(FullSearchResult searchResult)
    {
        super();
        this.searchResult = searchResult;
        mPlayer = StarokApplication.getAppContext().getComponent().mediaPlayer();
    }


    @Override
    protected int getSectionCount()
    {
        if (searchResult != null)
        {
            return searchResult.getContent().size();
        }
        return 0;
    }

    @Override
    protected int getItemCountForSection(int section)
    {
        return searchResult.getContent().get(section).getList().size();
    }

    @Override
    protected boolean hasFooterInSection(int section)
    {
        return false;
    }

    @Override
    protected SearchResultAdapter.HeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent,
                                                                                   int viewType)
    {
        SearchPopupHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                   R.layout.search_popup_header,
                                                                   null,
                                                                   false);
        return new HeaderViewHolder(binding);
    }

    @Override
    protected SearchResultAdapter.FooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent,
                                                                                   int viewType)
    {
        return null;
    }

    @Override
    protected SearchResultAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent,
                                                                        int viewType)
    {

        SearchPopupItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                 R.layout.search_popup_item,
                                                                 null,
                                                                 false);
        binding.recyclerviewSwipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        return new ItemViewHolder(binding);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(SearchResultAdapter.HeaderViewHolder holder,
                                                 int section)
    {
        FullSearchResult.ContentItem item = searchResult.getContent().get(section);
        holder.binding.title.setText(item.getTitle());
    }

    @Override
    protected void onBindSectionFooterViewHolder(SearchResultAdapter.FooterViewHolder holder,
                                                 int section)
    {

    }

    @Override
    public int getItemViewType(int position)
    {
        return super.getItemViewType(position);
    }

    @Override
    protected void onBindItemViewHolder(SearchResultAdapter.ItemViewHolder holder,
                                        int section,
                                        int position)
    {
        FullSearchResult.ContentItem item = searchResult.getContent().get(section);
        Object                       o    = item.getList().get(position);
        if (o instanceof Song)
        {
            Song s = (Song) o;
            holder.binding.songInfo.setVisibility(View.VISIBLE);
            holder.binding.title.setText(s.getName());
            holder.binding.singerName.setText(s.getSingerNames());
            holder.binding.btnTop.setOnClickListener(v -> {
                mPlayer.AddFistMedia(s);

            });
            holder.binding.btnDelete.setOnClickListener(v -> mPlayer.delMedia(s));
            holder.binding.content.setOnClickListener(v -> mPlayer.addLastMedia(s));
            BindingUtils.songImage(holder.binding.img, s.getId() + "/100/100");
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
                    holder.binding.textFlag.setText("--预约 " + String.format("%03d",
                                                                            mPlayer.selectIndex(s) + 1));
                    holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                        .getColor(R.color.search_item_flag));
                    holder.binding.btnTop.setVisibility(View.VISIBLE);
                    holder.binding.btnDelete.setVisibility(View.VISIBLE);
                    break;
                case TYPE_TOP:
                    holder.binding.textFlag.setVisibility(View.VISIBLE);
                    holder.binding.textFlag.setText("--下首播放 ");
                    holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                        .getColor(R.color.search_item_flag));
                    holder.binding.btnTop.setVisibility(View.GONE);
                    holder.binding.btnDelete.setVisibility(View.VISIBLE);
                    break;
                case TYPE_PLAYING:
                    holder.binding.textFlag.setVisibility(View.VISIBLE);
                    holder.binding.textFlag.setText("--正播放");
                    holder.binding.textFlag.setTextColor(holder.itemView.getContext().getResources()
                                                                        .getColor(R.color.search_item_flag));
                    holder.binding.btnTop.setVisibility(View.GONE);
                    holder.binding.btnDelete.setVisibility(View.GONE);
                    break;
            }
        }
        else if (o instanceof Singer)
        {
            holder.binding.title.setText(((Singer) o).getSimpName());
            holder.binding.songInfo.setVisibility(View.GONE);
            holder.binding.content.setOnClickListener(v -> EventBus.getDefault()
                                                                   .post(new SearchSingerItemClickEvent(
                                                                           (Singer) o)));

            BindingUtils.loadSingImage(holder.binding.img, ((Singer) o).getImg(), "100");
//            holder.binding.mthisask.setOnClickListener(v -> EventBus.getDefault()
//                                                                .post(new SearchSingerItemClickEvent(
//                                                                        (Singer) o)));
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        final SearchPopupHeaderBinding binding;

        public HeaderViewHolder(SearchPopupHeaderBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        final SearchPopupItemBinding binding;

        public ItemViewHolder(SearchPopupItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    private SongListAdapter.SongItemType getItemType(Song song)
    {
        if (song.equals(mPlayer.getMedia()))
        {
            return SongListAdapter.SongItemType.TYPE_PLAYING;
        }
        if (mPlayer.isSelected(song))
        {
            if (mPlayer.selectIndex(song) == 0)
            {
                return SongListAdapter.SongItemType.TYPE_TOP;
            }
            return SongListAdapter.SongItemType.TYPE_SELECTED;
        }
        return SongListAdapter.SongItemType.TYPE_NONE;
    }
}
