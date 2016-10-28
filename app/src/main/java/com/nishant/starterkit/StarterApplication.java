package com.nishant.starterkit;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.nishant.starterkit.data.PersonRepositoryModule;
import com.nishant.starterkit.injection.component.ApplicationComponent;
import com.nishant.starterkit.injection.component.DaggerApplicationComponent;
import com.nishant.starterkit.injection.module.ApplicationModule;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class StarterApplication extends Application {

  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
    Stetho.initializeWithDefaults(this);
    Fabric.with(this, new Crashlytics());
  }

  public static StarterApplication get(Context context) {
    return (StarterApplication) context.getApplicationContext();
  }

  public ApplicationComponent getComponent() {
    if (mApplicationComponent == null) {
      mApplicationComponent = DaggerApplicationComponent.builder()
        .personRepositoryModule(new PersonRepositoryModule(this))
        .applicationModule(new ApplicationModule(this)).build();
    }
    return mApplicationComponent;
  }
}
