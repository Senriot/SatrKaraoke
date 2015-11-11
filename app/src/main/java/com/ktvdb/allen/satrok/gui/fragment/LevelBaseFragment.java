package com.ktvdb.allen.satrok.gui.fragment;


import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fragmentmaster.app.IMasterFragment;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.utils.FragmnetTitleHelper;
import com.rey.material.widget.Button;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class LevelBaseFragment<DB extends ViewDataBinding> extends AbstractFragment<DB>
{
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        final LinearLayout topBar = (LinearLayout) rootView.findViewById(R.id.nav_bar);
        rootView.findViewById(R.id.go_back_main).setOnClickListener(v -> {
            for (int i = getFragmentMaster().getFragments().size() - 1; i > 0; i--)
            {
                IMasterFragment f = getFragmentMaster().getFragments().get(i);
                if (!(f instanceof MainFragment))
                {
                    f.finish();
                }
                else
                {
                    break;
                }
            }
        });
        List<IMasterFragment> list = getFragmentMaster().getFragments();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                               40);

        layoutParams.setMargins(-13, 0, 0, 0);
        for (int i = 1; i < list.size(); i++)
        {
            IMasterFragment fragment = list.get(i);
            String title = FragmnetTitleHelper.getFragmnetTitle(getActivity(), fragment);
            Button b = (Button) View.inflate(getActivity(), R.layout.nav_button, null);
            b.setTag(fragment);
            b.setOnClickListener(v -> {
                IMasterFragment fragment1 = (IMasterFragment) v.getTag();
                int index = list.indexOf(fragment1);
                for (int j = list.size() - 1; j > index; j--)
                {
                    list.get(j).finish();
                }
            });
            b.setText(title);
            b.setPadding(30, 0, 30, 0);
            topBar.addView(b, layoutParams);
        }
        String title = FragmnetTitleHelper.getFragmnetTitle(getActivity(), this);
        Button b     = (Button) View.inflate(getActivity(), R.layout.nav_button, null);
        b.setBackgroundResource(R.drawable.nav_current);
        b.setText(title);
        b.setPadding(30, 0, 30, 0);
        topBar.addView(b, layoutParams);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

    }


    @OnClick(R.id.go_back_main)
    public void goMain(View view)
    {
        for (int i = getFragmentMaster().getFragments().size() - 1; i > 0; i--)
        {
            IMasterFragment f = getFragmentMaster().getFragments().get(i);
            if (!(f instanceof MainFragment))
            {
                getFragmentMaster().finishFragment(f, 0, null);
            }
            else
            {
                break;
            }
        }
    }
}
