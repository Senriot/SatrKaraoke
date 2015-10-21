package com.ktvdb.allen.satrok.bind;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ktvdb.allen.satrok.BR;

/**
 * Created by Allen on 15/8/30.
 */
public class PlayHubModel extends BaseObservable
{
    @Bindable
    private String songName;
    @Bindable
    private String singerName;

    @Bindable
    private String currentTime;

    @Bindable
    private int max;

    @Bindable
    private int position;

    @Bindable
    private String endTime;

    @Bindable
    private String selectedCount;

    @Bindable
    private String imageUrl;

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public String getSelectedCount()
    {
        return selectedCount;
    }

    public void setSelectedCount(String selectedCount)
    {
        this.selectedCount = selectedCount;
        notifyPropertyChanged(BR.selectedCount);
    }

    public String getCurrentTime()
    {
        return currentTime;
    }

    public void setCurrentTime(String currentTime)
    {
        this.currentTime = currentTime;
        notifyPropertyChanged(BR.currentTime);
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
        notifyPropertyChanged(BR.endTime);
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
        notifyPropertyChanged(BR.songName);
    }

    public String getSingerName()
    {
        return singerName;
    }

    public void setSingerName(String singerName)
    {
        this.singerName = singerName;
        notifyPropertyChanged(BR.singerName);
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max)
    {
        this.max = max;
        notifyPropertyChanged(BR.max);
    }
}
