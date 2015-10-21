package com.ktvdb.allen.satrok.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/9/12.
 */
public class FullSearchResult
{
    private List<ContentItem> content = new ArrayList<>();

    public List<ContentItem> getContent()
    {
        return content;
    }

    private List<Singer> singers;

    private List<Song> songs;

    public List<Singer> getSingers()
    {
        return singers;
    }

    public void setSingers(List<Singer> singers)
    {
        this.singers = singers;
    }

    public List<Song> getSongs()
    {
        return songs;
    }

    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }

    public void setContent(List<ContentItem> content)
    {
        this.content = content;
    }

    public void addItem(ContentItem item)
    {
        content.add(item);
    }

    public static class ContentItem <T>
    {
        private String title;

        private List<T> list;

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public List<T> getList()
        {
            return list;
        }

        public void setList(List<T> list)
        {
            this.list = list;
        }
    }
}
