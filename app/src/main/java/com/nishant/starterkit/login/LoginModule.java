package com.nishant.starterkit.login;

import android.content.SharedPreferences;

import com.nishant.starterkit.SmartHomeService;
import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.module.ActivityModule;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module(includes = ActivityModule.class)
public class LoginModule {

  @Provides
  public LoginPresenter provideLoginPresenter(
    SmartHomeService mSmartHomeService,
    SharedPreferences mSharedPreferences
  ) {
    return new LoginPresenter(mSmartHomeService, mSharedPreferences);
  }


}
