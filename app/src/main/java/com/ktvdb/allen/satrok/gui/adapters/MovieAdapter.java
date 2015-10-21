package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.MovieGridItemViewBinding;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.utils.ViewUtils;
import com.rey.material.widget.Button;

import org.apache.commons.lang3.StringUtils;
import org.simple.eventbus.EventBus;

import butterknife.Bind;

/**
 * Created by Allen on 15/7/12.
 */
public class MovieAdapter extends NewokMediaAdapter<MovieAdapter.ViewHolder, Movie>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final MovieGridItemViewBinding binding = DataBindingUtil.inflate(inflater,
                                                                        R.layout.movie_grid_item_view,
                                                                        parent,
                                                                        false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.setItem(getItem(position));
    }


    public class ViewHolder extends NewokBaseViewHolder<Movie> implements View.OnClickListener
    {

        final MovieGridItemViewBinding binding;

        public ViewHolder(View itemView, MovieGridItemViewBinding binding)
        {
            super(itemView);
            this.binding = binding;
            binding.mask.setOnClickListener(this);
        }


        public void setItem(Movie movie)
        {
            super.setItem(movie);
            binding.setMovie(movie);
            Uri uri;
            if (!StringUtils.isEmpty(movie.getImg()))
            {
                uri = Uri.parse(movie.getImg());
            }
            else
            {
                uri = Uri.parse(
                        "android.resource://" + StarokApplication.getAppContext().getPackageName() +
                                "/" + R.drawable.singer_def);
            }
            binding.movieImage.setImageURI(uri);
        }

        @Override
        public void onClick(View v)
        {
            if (!ViewUtils.isFastDoubleClick())
                EventBus.getDefault().post(getItem(), "showDetail");
        }
    }
}
