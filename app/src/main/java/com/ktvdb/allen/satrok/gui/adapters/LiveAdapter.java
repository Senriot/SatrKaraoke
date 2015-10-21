package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.SateliteGridItemBinding;
import com.ktvdb.allen.satrok.gui.fragment.SatelliteLiveFragment;
import com.ktvdb.allen.satrok.model.LiveProgram;
import com.ktvdb.allen.satrok.utils.ViewUtils;

/**
 * Created by Allen on 15/7/11.
 */
public class LiveAdapter extends NewokMediaAdapter<LiveAdapter.ViewHolder, LiveProgram>
{
    private final SatelliteLiveFragment mFragment;

    public LiveAdapter(SatelliteLiveFragment mFragment) {this.mFragment = mFragment;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final SateliteGridItemBinding binding = DataBindingUtil.inflate(inflater,
                                                                        R.layout.satelite_grid_item,
                                                                        parent,
                                                                        false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.bind(getItem(position));
        holder.binding.mask.setOnClickListener(v -> {
            if (!ViewUtils.isFastDoubleClick())
            {
                mFragment.onItemClickListener(getItem(position));
            }
        });
    }

    public static class ViewHolder extends NewokBaseViewHolder<LiveProgram>
    {
        final SateliteGridItemBinding binding;

        public ViewHolder(View itemView, SateliteGridItemBinding binding)
        {
            super(itemView);
            this.binding = binding;
            binding.avatar.setAspectRatio(1.0f);
        }

        public void bind(LiveProgram program)
        {
            this.binding.setLive(program);
        }
    }
}
