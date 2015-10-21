package com.ktvdb.allen.satrok.gui.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.BillDetailItenBinding;
import com.ktvdb.allen.satrok.model.BillDetail;

/**
 * Created by Allen on 15/9/21.
 */
public class BillDetailAdapter extends BaseAdapter<BillDetailAdapter.ViewHolder, BillDetail.Datum>
{
    @Override
    public BillDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        BillDetailItenBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                                R.layout.bill_detail_iten,
                                                                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BillDetailAdapter.ViewHolder holder, int position)
    {
        holder.binding.setItem(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        final BillDetailItenBinding binding;

        public ViewHolder(BillDetailItenBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
