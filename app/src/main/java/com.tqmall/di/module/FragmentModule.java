package com.tqmall.di.module;

import android.app.Activity;
import android.content.Context;
import android.app.Fragment;
import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerFragment;
import dagger.Module;
import dagger.Provides;


@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PerFragment
    @ContextLife("Activity")
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @PerFragment
    public Fragment provideFragment() {
        return mFragment;
    }
}
