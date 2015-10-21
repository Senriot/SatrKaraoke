package com.ktvdb.allen.satrok.module;

import com.ktvdb.allen.satrok.model.SongQueryCondition;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Allen on 15/8/30.
 */
@Module
public class SongListModule
{
    @Provides
    SongQueryCondition providesSongQueryCondition()
    {
        return new SongQueryCondition();
    }
}
