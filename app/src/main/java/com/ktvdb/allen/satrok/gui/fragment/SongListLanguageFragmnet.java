package com.ktvdb.allen.satrok.gui.fragment;

import android.view.View;
import android.widget.RadioGroup;

import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.model.Direction;
import com.ktvdb.allen.satrok.model.SongQueryCondition;

/**
 * Created by Allen on 15/9/2.
 */
@FragmnetTitle("语种点歌")
public class SongListLanguageFragmnet extends SongListBaseFragment
{
    @Override
    protected void init()
    {
        replacePage(new SongQueryCondition("hot", Direction.DESC,
                                           SongQueryCondition.SongCategory.None, null));
    }

    @Override
    protected void onLoadTabView()
    {
        onLoadLanguage();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_all)
        {
            replacePage(new SongQueryCondition("hot", Direction.DESC,
                                               SongQueryCondition.SongCategory.None, null));
        }
        else
        {
            View view = group.findViewById(checkedId);
            replacePage(new SongQueryCondition("hot",
                                               Direction.DESC,
                                               SongQueryCondition.SongCategory.Language,
                                               (String) view.getTag()));
        }
    }
}
