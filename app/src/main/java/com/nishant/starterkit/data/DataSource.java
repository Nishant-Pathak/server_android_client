package com.nishant.starterkit.data;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.model.Person;

import java.util.List;

import rx.Observable;

/**
 * Main point to enter to data repository
 */

public interface DataSource {
  Observable<Person> getPerson(@NonNull Long personId);
  Observable<List<Person>> getPersons(boolean forced);
  Observable<Person> addPerson(@NonNull Person person);
}
