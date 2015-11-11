package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.badoo.mobile.util.WeakHandler;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.FragmentBindWeiXinBinding;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.model.SongQueryCondition;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
@FragmnetTitle("微互动")
public class BindWeiXinFragment extends LevelBaseFragment<FragmentBindWeiXinBinding> implements RadioGroup.OnCheckedChangeListener
{

    private WeakHandler mHandler;

    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_bind_wei_xin;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
        mHandler = new WeakHandler();
        mHandler.postDelayed(() -> {
            replacePage(R.drawable.weihudong);
            Picasso.with(getActivity())
                   .load(R.drawable.weihudong_ad)
                   .into(mBinding.adImage);
        }, 200);

    }

    public void replacePage(int resId)
    {
        WeiHuDongPageFragment fragment    = WeiHuDongPageFragment.newInstance(resId);
        FragmentTransaction   transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_WeiHuDong)
        {
            replacePage(R.drawable.weihudong);
        }
        else
        {
            replacePage(R.drawable.weihudong_fangfa);
        }
    }
}
