package com.tqmall.di.module;

import android.app.Service;
import android.content.Context;


import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerService;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    @ContextLife("Service")
    public Context ProvideServiceContext() {
        return mService;
    }
}
