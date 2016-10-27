package com.nishant.starterkit.data;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.model.Person;
import com.nishant.starterkit.injection.annotation.Cache;
import com.nishant.starterkit.injection.annotation.Local;
import com.nishant.starterkit.injection.annotation.Remote;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class PersonRepository implements DataSource {

  private final DataSource localDataSource;

  private final DataSource remoteDataSource;

  private final DataSource cacheDataSource;

  @Inject
  public PersonRepository(
    @Local @NonNull DataSource localDataSource,
    @Remote @NonNull DataSource remoteDataSource,
    @Cache @NonNull DataSource cacheDataSource
  ) {
    this.localDataSource = localDataSource;
    this.remoteDataSource = remoteDataSource;
    this.cacheDataSource = cacheDataSource;
  }

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    Observable<Person> cacheObservable = cacheDataSource.getPerson(personId);
    Observable<Person> localObservable = localDataSource.getPerson(personId);
    Observable<Person> remoteObservable =
      remoteDataSource.getPerson(personId)
        .doOnNext(localDataSource::addPerson);

    return Observable.concat(
      cacheObservable,
      localObservable,
      remoteObservable
    )
      .first(person -> person != null);
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    Observable<List<Person>> localObservable = localDataSource.getPersons(forced);
    Observable<List<Person>> remoteObservable =
      remoteDataSource.getPersons(forced)
        .doOnNext(persons -> {
          for(Person person: persons) {
            localDataSource.addPerson(person);
          }
        });
    if (forced) {
      return remoteObservable;
    }

    Observable<List<Person>> cacheObservable = cacheDataSource.getPersons(false);
    return Observable.concat(
      cacheObservable,
      localObservable,
      remoteObservable
    ).first(persons -> !persons.isEmpty());
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    return remoteDataSource.addPerson(person).doOnNext(localDataSource::addPerson);
  }
}
