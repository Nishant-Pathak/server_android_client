package com.nishant.starterkit.data.cache;

import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;

import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.BehaviorSubject;

@Singleton
public class CacheDataSource implements DataSource {

  private final LongSparseArray<Person> personsCache = new LongSparseArray<>(10);
  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    return Observable.just(personsCache.get(personId));
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    List<Person> personList = new ArrayList<>(personsCache.size());
    for(int i = 0; i <personsCache.size(); i++) {
      personList.add(personsCache.get(i));
    }
    return Observable.create(subscriber -> {
      subscriber.onNext(personList);
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    BehaviorSubject<Person> behaviorSubject = BehaviorSubject.create();
    personsCache.put(person._id(), person);
    behaviorSubject.onNext(person);
    behaviorSubject.onCompleted();
    return behaviorSubject;
  }
}
