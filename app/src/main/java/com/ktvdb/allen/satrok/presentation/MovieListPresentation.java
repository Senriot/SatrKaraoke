package com.ktvdb.allen.satrok.presentation;

import com.ktvdb.allen.satrok.gui.MainActivity;
import com.ktvdb.allen.satrok.gui.adapters.MovieAdapter;
import com.ktvdb.allen.satrok.model.Movie;
import com.ktvdb.allen.satrok.model.MovieQueryCondition;
import com.ktvdb.allen.satrok.model.PageResponse;
import com.ktvdb.allen.satrok.model.SysDictionary;
import com.ktvdb.allen.satrok.presentation.view.MovieListView;
import com.ktvdb.allen.satrok.service.RestService;

import java.util.List;

import javax.inject.Inject;

import autodagger.AutoInjector;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Allen on 15/8/28.
 */
@AutoInjector(MainActivity.class)
public class MovieListPresentation
{
    @Inject
    RestService restService;

    MovieAdapter mAdapter = new MovieAdapter();

    MovieQueryCondition mCondition = new MovieQueryCondition();

    final MovieListView mView;

    public MovieListPresentation(MovieListView mView)
    {
        MainActivity.getComponent().inject(this);
        this.mView = mView;
    }


    public void initData()
    {

        restService.getMovieTypes().subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<List<SysDictionary>>empty())
                   .subscribe(mView::updateTabView);
        loadMovies(moviePageResponse -> {
            mAdapter.addAll(moviePageResponse.getContent());
            mView.initListView(mAdapter);
        });
    }

    public void refreshMovies(String type)
    {
    }

    private void loadMovies(Action1<PageResponse<Movie>> action)
    {
        restService.getMovies(mCondition).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .onErrorResumeNext(Observable.<PageResponse<Movie>>empty())
                   .subscribe(action);
    }

    public void reLoadMovies(String type)
    {
        mCondition.setTypeCode(type);
        mCondition.setPage(0);
        mAdapter.clear();
        loadMovies(moviePageResponse -> mAdapter.addAll(moviePageResponse.getContent()));
    }
}
