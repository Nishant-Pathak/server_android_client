package com.nishant.starterkit.person;

import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.component.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = PersonsModule.class)
public interface PersonsComponent {
  void inject(PersonsActivity activity);
}
