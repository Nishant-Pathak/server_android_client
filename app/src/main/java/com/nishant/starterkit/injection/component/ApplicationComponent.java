package com.nishant.starterkit.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.PersonRepositoryModule;
import com.nishant.starterkit.injection.annotation.ApplicationContext;
import com.nishant.starterkit.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    ApplicationModule.class,
    PersonRepositoryModule.class
  }
)
public interface ApplicationComponent {

  @ApplicationContext Context context();
  Application application();

  PersonRepository personRepository();

  SharedPreferences sharedPreferences();
}
