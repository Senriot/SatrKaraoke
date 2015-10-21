package com.ktvdb.allen.satrok.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ktvdb.allen.satrok.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/8/31.
 */
public class HWBoardViewModel extends BaseObservable
{
    @Bindable
    private char[] results = new char[8];

    public HWBoardViewModel()
    {
//        results = new ArrayList<>();
//        results.add("");
//        results.add("");
//        results.add("");
//        results.add("");
//        results.add("");
//        results.add("");
//        results.add("");
//        results.add("");
    }

    public char[] getResults()
    {
        return results;
    }

    public void setResults(char[] results)
    {
        this.results = results;
        notifyPropertyChanged(BR.results);
    }

    //    public List<String> getResults()
//    {
//        return results;
//    }
//
//    public void setResults(List<String> results)
//    {
//        this.results = results;
//    }

//    public void addChars(char[] restlt)
//    {
//        results.clear();
//        for (char c : restlt)
//        {
//            results.add(String.valueOf(c));
//        }
//
//        notifyPropertyChanged(BR.results);
//    }
}
