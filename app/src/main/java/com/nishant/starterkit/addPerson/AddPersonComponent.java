package com.nishant.starterkit.addPerson;

import com.nishant.starterkit.injection.annotation.PerActivity;
import com.nishant.starterkit.injection.component.ApplicationComponent;

import dagger.Component;

@PerActivity
@Component(modules = AddPersonModule.class, dependencies = ApplicationComponent.class)
public interface AddPersonComponent {
  void inject(AddPersonActivity activity);
}
