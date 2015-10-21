package com.ktvdb.allen.satrok.gui.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Allen on 15/7/17.
 */
public abstract class NewokMediaAdapter<VH extends NewokBaseViewHolder<T>, T> extends RecyclerView.Adapter<VH>
{
    private List<T> mList = new ArrayList<>();

    public void add(T meida)
    {
        mList.add(meida);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> collection)
    {
        mList.addAll(collection);
        notifyDataSetChanged();
    }

    public void inster(int position, T media)
    {
        mList.add(position, media);
        notifyDataSetChanged();
    }

    public void remove(T media)
    {
        mList.remove(media);
        notifyDataSetChanged();
    }

    public void remove(int position)
    {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public void clear()
    {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    public T getItem(int position)
    {
        return mList.get(position);
    }

    public void setList(List<T> mList)
    {
        this.mList = mList;
        notifyDataSetChanged();
    }
}
