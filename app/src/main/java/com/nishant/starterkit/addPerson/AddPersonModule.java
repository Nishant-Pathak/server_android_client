package com.nishant.starterkit.addPerson;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.module.ActivityModule;

import dagger.Module;
import dagger.Provides;

@PerActivity
@Module(includes = ActivityModule.class)
public class AddPersonModule {

  @Provides AddPersonPresenter provideAddPersonPresenter(PersonRepository personRepository) {
    return new AddPersonPresenter(personRepository);
  }
}
