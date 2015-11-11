package com.ktvdb.allen.satrok.gui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ktvdb.allen.satrok.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeiHuDongPageFragment extends Fragment
{


    @Bind(R.id.content_image)
    ImageView contentImage;

    private int resId;


    public static WeiHuDongPageFragment newInstance(int resId)
    {
        WeiHuDongPageFragment fragment = new WeiHuDongPageFragment();
        fragment.resId = resId;
        return fragment;
    }

    public WeiHuDongPageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_wei_hu_dong_page, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Picasso.with(getContext())
               .load(resId)
               .into(contentImage);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
