package com.tqmall.di.component;

import android.content.Context;

import com.tqmall.di.module.ApplicationModule;
import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerApp;

import dagger.Component;

@PerApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ContextLife("Application")
    Context getApplication();

}

