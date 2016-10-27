package com.nishant.starterkit.login;

import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.component.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = LoginModule.class)
public interface LoginComponent {
  void inject(LoginActivity loginActivity);
}
