package com.nishant.starterkit.injection.module;

import android.app.Activity;
import android.content.Context;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.injection.annotation.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
  private Activity mActivity;

  public ActivityModule(Activity activity) {
    mActivity = activity;
  }

  @Provides
  Activity provideActivity() {
    return mActivity;
  }

  @Provides
  @ActivityContext
  Context provideContext() {
    return mActivity;
  }
}