package com.ktvdb.allen.satrok.gui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Allen on 15/7/17.
 */
public class NewokBaseViewHolder<T> extends RecyclerView.ViewHolder
{
    private T item;

    public NewokBaseViewHolder(View itemView)
    {
        super(itemView);
    }

    public T getItem()
    {
        return item;
    }

    public void setItem(T item)
    {
        this.item = item;
    }
}
