package com.nishant.starterkit.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nishant.starterkit.R;
import com.nishant.starterkit.injection.annotation.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ApplicationModule {
  private final Application mApplication;

  public ApplicationModule(Application mApplication) {
    this.mApplication = mApplication;
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
  SharedPreferences provideSharedPreferences() {
    return
      mApplication
        .getApplicationContext()
        .getSharedPreferences(mApplication.getString(R.string.pref_file_name), Context.MODE_PRIVATE);
  }
}
