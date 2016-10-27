package com.nishant.starterkit.person;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.module.ActivityModule;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module(includes = ActivityModule.class)
public class PersonsModule {

  @Provides PersonsPresenter providePersonPresenter(PersonRepository personRepository) {
    return new PersonsPresenter(personRepository);
  }
}
