package com.tqmall.di.component;

import android.app.Activity;
import android.content.Context;
import com.tqmall.di.module.FragmentModule;
import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerFragment;
import dagger.Component;


@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

}
