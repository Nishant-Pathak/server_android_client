package com.nishant.starterkit.data.local;

import android.provider.Settings;
import android.support.test.InstrumentationRegistry;

import com.nishant.starterkit.data.model.Person;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class LocalDataSourceTest {

  private static final String FIRST_NAME1 = "first_name1";
  private static final String LAST_NAME1 = "last_name1";

  private static final String FIRST_NAME2 = "first_name2";
  private static final String LAST_NAME2 = "last_name2";

  private static final String FIRST_NAME3 = "first_name3";
  private static final String LAST_NAME3 = "last_name3";

  private LocalDataSource mLocalDataSource;

  @Before
  public void setUp() throws Exception {
    PersonOpenHelper personOpenHelper = new PersonOpenHelper(InstrumentationRegistry.getContext());
    mLocalDataSource = new LocalDataSource(personOpenHelper);

  }

  @Test
  public void addPerson_getPerson() throws Exception {
    final Person person1 = Person.FACTORY.creator.create(0, FIRST_NAME1, LAST_NAME1);
    mLocalDataSource.addPerson(person1);
    mLocalDataSource.getPerson(0L).subscribe(person -> {
      assertThat(person1, is(person));
    });
  }

  @Test
  public void addPerson_addPerson() throws Exception {
    final Person person1 = Person.FACTORY.creator.create(0, FIRST_NAME1, LAST_NAME1);
    mLocalDataSource.addPerson(person1);
    mLocalDataSource.addPerson(person1).subscribe(new Subscriber<Person>() {
      @Override
      public void onCompleted() {
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(Person person) {
        fail("expected:  UNIQUE constraint failed");
      }
    });
  }

  @Test
  public void addPersons_getPersons() throws Exception {
    final Person person1 = Person.FACTORY.creator.create(0, FIRST_NAME1, LAST_NAME1);
    final Person person2 = Person.FACTORY.creator.create(1, FIRST_NAME2, LAST_NAME2);
    final Person person3 = Person.FACTORY.creator.create(2, FIRST_NAME3, LAST_NAME3);
    mLocalDataSource.addPerson(person1);
    mLocalDataSource.addPerson(person2);
    mLocalDataSource.addPerson(person3);
    Observable<List<Person>> persons = mLocalDataSource.getPersons(false);
    persons.subscribe(personList -> {
      assertThat(personList, hasSize(3));
      assertThat(personList, hasItem(person1));
      assertThat(personList, hasItem(person2));
      assertThat(personList, hasItem(person3));
    });
  }
}