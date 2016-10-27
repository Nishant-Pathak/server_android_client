package com.nishant.starterkit.data.remote;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class FakeRemoteDataSource implements DataSource {

  private static final Map<Long, Person> TASKS_SERVICE_DATA = new LinkedHashMap<>();
  private static Long id = 0l;

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    Observable<Person> personObservable = Observable.create(subscriber -> {
      subscriber.onNext(TASKS_SERVICE_DATA.get(personId));
      subscriber.onCompleted();
    });
    return personObservable.delay(5, TimeUnit.SECONDS);
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    Observable<List<Person>> personsObservable = Observable.create(subscriber -> {
      List<Person> personList = new ArrayList<>(TASKS_SERVICE_DATA.size());
      personList.addAll(TASKS_SERVICE_DATA.values());
      subscriber.onNext(personList);
      subscriber.onCompleted();
    });

    return personsObservable.delay(5, TimeUnit.SECONDS);
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    Observable<Person> personObservable = Observable.create(subscriber -> {
      Long currentId = id++;
      Person newPerson = Person.FACTORY.creator.create(currentId, person.first_name(), person.last_name());
      TASKS_SERVICE_DATA.put(currentId, newPerson);
      subscriber.onNext(TASKS_SERVICE_DATA.get(currentId));
      subscriber.onCompleted();
    });
    return personObservable.delay(5, TimeUnit.SECONDS);
  }
}
