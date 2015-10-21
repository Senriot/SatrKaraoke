package com.ktvdb.allen.satrok.gui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;

/**
 * Created by Allen on 15/10/16.
 */
public class SearchHeaderAdapter implements StickyHeadersAdapter<SearchHeaderAdapter.ViewHolder>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {

    }

    @Override
    public long getHeaderId(int position)
    {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
