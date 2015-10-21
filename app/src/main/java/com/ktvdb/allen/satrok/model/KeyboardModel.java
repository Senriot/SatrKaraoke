package com.ktvdb.allen.satrok.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ktvdb.allen.satrok.BR;
import com.ktvdb.allen.satrok.gui.widget.KeyBoard;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Allen on 15/9/1.
 */
public class KeyboardModel extends BaseObservable
{
    private final KeyBoard.OnTextChangedLinstener textChangedLinstener;

    @Bindable
    private String searchText = "";

    public KeyboardModel(KeyBoard.OnTextChangedLinstener textChangedLinstener) {this.textChangedLinstener = textChangedLinstener;}

    public String getSearchText()
    {
        return searchText;
    }

    public void setSearchText(String searchText)
    {
        this.searchText = searchText;
        notifyPropertyChanged(BR.searchText);
    }

    public void addSearchText(String text)
    {
        if (StringUtils.isNoneBlank(text))
        {
            searchText += text;
            notifyPropertyChanged(BR.searchText);
        }

    }

    public void removeSearchText()
    {
        if (!StringUtils.isEmpty(searchText))
        {
            searchText = searchText.trim();
            searchText = searchText.substring(0, searchText.length() - 1).trim();
            notifyPropertyChanged(BR.searchText);
        }
    }

    public KeyBoard.OnTextChangedLinstener getTextChangedLinstener()
    {
        return textChangedLinstener;
    }

    @Override
    public void notifyPropertyChanged(int fieldId)
    {
        super.notifyPropertyChanged(fieldId);
        textChangedLinstener.onTextChanged(searchText);
//        if (fieldId == BR.searchText)
//        {
//            EventBus.getDefault().post(new SearchInputEvent(searchText));
//        }
    }
}
