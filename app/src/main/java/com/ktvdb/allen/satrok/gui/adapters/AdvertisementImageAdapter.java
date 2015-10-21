package com.ktvdb.allen.satrok.gui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.ImageadPageItemBinding;
import com.ktvdb.allen.satrok.model.Advertisement;

import java.util.List;

/**
 * Created by Allen on 15/9/19.
 */
public class AdvertisementImageAdapter extends RecyclingPagerAdapter
{
    private final List<Advertisement> advertisements;
    private final Context             context;

    public AdvertisementImageAdapter(List<Advertisement> advertisements, Context context)
    {
        this.advertisements = advertisements;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            ImageadPageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                                                                             context),
                                                                     R.layout.imagead_page_item,
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
        holder.bind(advertisements.get(position));
        return convertView;
    }

    @Override
    public int getCount()
    {
        return advertisements.size();
    }


    private static class ViewHolder
    {
        private final ImageadPageItemBinding binding;

        private ViewHolder(ImageadPageItemBinding binding) {this.binding = binding;}

        public void bind(Advertisement advertisement)
        {
            binding.setAdvertisement(advertisement);
            binding.executePendingBindings();
        }
    }
}
