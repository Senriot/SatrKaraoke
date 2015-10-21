package com.ktvdb.allen.satrok.gui.widget;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.HwBoardViewBinding;
import com.ktvdb.allen.satrok.model.HWBoardViewModel;
import com.ktvdb.allen.satrok.model.Movie;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Allen on 15/8/31.
 */
public class HWBoardView extends FrameLayout implements PaintView.OnResultListener
{
    HwBoardViewBinding mBinding;

    HWBoardViewModel model;

    public interface OnTextSelectedLinstener
    {
        void OnTextSelectedLinstener(String text);
    }

    private OnTextSelectedLinstener linstener;

    public OnTextSelectedLinstener getLinstener()
    {
        return linstener;
    }

    public void setLinstener(OnTextSelectedLinstener linstener)
    {
        this.linstener = linstener;
    }

    public HWBoardView(Context context)
    {
        this(context, null);
    }

    public HWBoardView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public HWBoardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                                           R.layout.hw_board_view,
                                           this,
                                           true);
        model = new HWBoardViewModel();
        mBinding.paintView.setListener(this);
        mBinding.setModel(model);

        ButterKnife.bind(this);
    }

    @Override
    public void onResult(char[] restlt)
    {
        model.setResults(restlt);
    }

    @OnClick(R.id.clear_button)
    void onClear()
    {
        mBinding.paintView.resetRecognize();
    }

    @OnClick({R.id.result1, R.id.result2, R.id.result3, R.id.result4, R.id.result5, R.id.result6, R.id.result7})
    void onTextClick(TextView button)
    {
        String text = button.getText().toString();
        LogUtils.e("selected:" + text);
        mBinding.paintView.resetRecognize();
        if (!StringUtils.isBlank(text) && linstener != null)
        {
            linstener.OnTextSelectedLinstener(text);
        }
    }


}
