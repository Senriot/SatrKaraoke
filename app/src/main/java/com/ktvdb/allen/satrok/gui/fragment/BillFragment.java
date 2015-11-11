package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentBillBinding;
import com.ktvdb.allen.satrok.erp.ERPNetworkService;
import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.BillDetailAdapter;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.gui.widget.DividerItemDecoration;
import com.ktvdb.allen.satrok.model.BillDetail;
import com.ktvdb.allen.satrok.utils.ConfigManager;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.functions.Action1;

/**
 * A simple {@link Fragment} subclass.
 */
@AutoInjector(MainActivity.class)
@FragmnetTitle("消费账单")
public class BillFragment extends LevelBaseFragment<FragmentBillBinding> implements Action1<BillDetail>
{

    @Inject
    ConfigManager     configManager;
    @Inject
    ERPNetworkService mService;

    private BillDetailAdapter mAdapter;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_bill;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.getComponent().inject(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager     layoutManager = new LinearLayoutManager(getContext());
        final SuperRecyclerView recyclerView  = mBinding.listView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                                                                 DividerItemDecoration.VERTICAL_LIST));


    }

    @Override
    public void call(BillDetail billDetail)
    {
        LogUtils.e(billDetail);
        mAdapter = new BillDetailAdapter();
        mAdapter.addAll(billDetail.getData());
        mBinding.listView.setAdapter(mAdapter);
    }
}
