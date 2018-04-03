package com.zac4j.yoda.di.module;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zac4j.yoda.App;
import com.zac4j.yoda.CurrentActivityProvider;
import com.zac4j.yoda.data.remote.ApiServer;
import com.zac4j.yoda.di.ActivityContext;
import com.zac4j.yoda.di.ApplicationContext;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Application Module
 * Created by zac on 16-7-3.
 */

@Module public class ApplicationModule {

    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @ActivityContext
    CurrentActivityProvider provideCurrentActivityProvider() {
        return (CurrentActivityProvider) mApplication;
    }

    @Provides
    ObjectMapper provideMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    ApiServer provideWebService() {
        return ApiServer.Factory.create(mApplication);
    }
}
