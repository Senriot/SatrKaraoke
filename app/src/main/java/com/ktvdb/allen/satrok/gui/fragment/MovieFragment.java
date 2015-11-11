package com.ktvdb.allen.satrok.gui.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.widget.GridLayoutManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fragmentmaster.app.Request;
import com.ktvdb.allen.satrok.R;
import com.ktvdb.allen.satrok.databinding.FragmentMovieBinding;
import com.ktvdb.allen.satrok.gui.adapters.MovieAdapter;
import com.ktvdb.allen.satrok.gui.annotation.FragmnetTitle;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.MovieListPresentation;
import com.ktvdb.allen.satrok.presentation.view.MovieListView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@FragmnetTitle("卫星影院")
public class MovieFragment extends LevelBaseFragment<FragmentMovieBinding> implements RadioGroup.OnCheckedChangeListener,
                                                                MovieListView
{

    MovieListPresentation mPresentation = new MovieListPresentation(this);


    @Override
    protected int getLayoutId()
    {
        return R.layout.fragment_movie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 8,
                                                                GridLayoutManager.VERTICAL, false);

        mBinding.recyclerView.setLayoutManager(layoutManager);
        mPresentation.initData();
    }

    @Override
    public void onDestroyView()
    {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId == R.id.rb_all)
        {
            mPresentation.reLoadMovies(null);
        }
        else
        {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            mPresentation.reLoadMovies((String) radioButton.getTag());
        }
    }

    @Subscriber(tag = "showDetail")
    void showDetail(Movie movie)
    {
        startFragment(new Request(MovieDetailFragment.class).putExtra("title", movie.getName())
                                                            .putExtra(Movie.class.getName(),
                                                                      movie));
    }

    @Override
    @UiThread
    public void updateTabView(List<SysDictionary> sysDictionaries)
    {
        int id = 100;
        for (SysDictionary type : sysDictionaries)
        {
            RadioButton radioButton = (RadioButton) getActivity().getLayoutInflater()
                                                                 .inflate(R.layout.tab_radiobutton,
                                                                          null);
            radioButton.setText(type.getDictName());
            radioButton.setId(id++);
            radioButton.setTag(type.getId());
            mBinding.categoryRadioGroup.addView(radioButton);
        }

        mBinding.categoryRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    @UiThread
    public void initListView(MovieAdapter mAdapter)
    {
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
