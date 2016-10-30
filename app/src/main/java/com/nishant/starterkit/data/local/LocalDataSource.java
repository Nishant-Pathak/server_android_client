package com.nishant.starterkit.data.local;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

@Singleton
public class LocalDataSource implements DataSource {

  @NonNull
  private BriteDatabase bdb;

  public LocalDataSource(PersonOpenHelper helper) {
    SqlBrite sqlBrite = SqlBrite.create();
    bdb = sqlBrite.wrapDatabaseHelper(helper , Schedulers.io());
  }

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    QueryObservable queryObservable =
      bdb.createQuery(Person.TABLE_NAME, Person.SELECT_BY_ID, String.valueOf(personId));
    return queryObservable.mapToOne(Person.ID_MAPPER::map);
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    QueryObservable queryObservable =
      bdb.createQuery(Person.TABLE_NAME, Person.SELECT_ALL);
    return queryObservable.mapToList(Person.ID_MAPPER::map).take(1);
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    BehaviorSubject<Person> subject = BehaviorSubject.create();
    try {
      bdb.insert(Person.TABLE_NAME, Person.FACTORY.marshal(person).asContentValues());
      subject.onNext(person);
    } catch (Exception ex) {
      subject.onError(ex);
    }
    subject.onCompleted();
    return subject;
  }
}
