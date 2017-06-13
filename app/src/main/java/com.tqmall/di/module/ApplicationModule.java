
package com.tqmall.di.module;

import android.content.Context;


import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerApp;
import com.tqmall.global.App;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private App mApplication;

    public ApplicationModule(App application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

}
