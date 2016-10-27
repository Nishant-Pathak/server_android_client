package com.nishant.starterkit.data.remote;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class RemoteDataSource implements DataSource {

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    return null;
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    return null;
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    return null;
  }
}
