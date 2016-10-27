package com.nishant.starterkit.injection.component;

import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.module.ActivityModule;
import com.nishant.starterkit.login.LoginActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
  void inject(LoginActivity loginActivity);
}
