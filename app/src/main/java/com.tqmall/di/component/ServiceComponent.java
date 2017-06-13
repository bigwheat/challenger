package com.tqmall.di.component;

import android.content.Context;
import com.tqmall.di.module.ServiceModule;
import com.tqmall.di.scope.ContextLife;
import com.tqmall.di.scope.PerService;
import dagger.Component;


@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    @ContextLife("Service")
    Context getServiceContext();

    @ContextLife("Application")
    Context getApplicationContext();
}
