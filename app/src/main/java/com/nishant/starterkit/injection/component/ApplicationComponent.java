package com.nishant.starterkit.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nishant.starterkit.SmartHomeService;
import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.PersonRepositoryModule;
import com.nishant.starterkit.injection.annotation.ApplicationContext;
import com.nishant.starterkit.injection.module.ApplicationModule;
import com.nishant.starterkit.injection.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
  modules = {
    ApplicationModule.class,
    PersonRepositoryModule.class,
    NetworkModule.class
  }
)
public interface ApplicationComponent {

  @ApplicationContext Context context();
  Application application();

  PersonRepository personRepository();

  SmartHomeService smartHomeService();

  SharedPreferences sharedPreferences();
}
