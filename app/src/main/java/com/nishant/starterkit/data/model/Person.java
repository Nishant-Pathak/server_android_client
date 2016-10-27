package com.nishant.starterkit.data.model;

import com.google.auto.value.AutoValue;
import com.nishant.starterkit.data.PersonModel;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class Person implements PersonModel {
  public static final Factory<Person> FACTORY = new Factory<>(AutoValue_Person::new);

  public static final RowMapper<Person> FIRST_NAME_MAPPER = FACTORY.select_by_first_nameMapper();
  public static final RowMapper<Person> ID_MAPPER = FACTORY.select_by_idMapper();
}
