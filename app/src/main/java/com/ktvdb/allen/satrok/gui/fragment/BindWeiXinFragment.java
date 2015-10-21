package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.StarokApplication;
import com.ktvdb.allen.satrok.databinding.FragmentBindWeiXinBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class BindWeiXinFragment extends LevelBaseFragment<FragmentBindWeiXinBinding> implements RadioGroup.OnCheckedChangeListener
{

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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_WeiHuDong)
        {
            mBinding.contentImage.setImageURI(Uri.parse("res://" + StarokApplication.getAppContext()
                                                                                    .getPackageName() + "/" + R.drawable.weihudong));
        }
        else
        {
            mBinding.contentImage.setImageURI(Uri.parse("res://" + StarokApplication.getAppContext()
                                                                                    .getPackageName() + "/" + R.drawable.weihudong_fangfa));
        }
    }
}
