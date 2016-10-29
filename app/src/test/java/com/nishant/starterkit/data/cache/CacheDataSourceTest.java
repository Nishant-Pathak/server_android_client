package com.nishant.starterkit.data.cache;

import com.nishant.starterkit.data.model.Person;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CacheDataSourceTest {

  private static final String FIRST_NAME1 = "first_name1";
  private static final String LAST_NAME1 = "last_name1";

  private static final String FIRST_NAME2 = "first_name2";
  private static final String LAST_NAME2 = "last_name2";

  private static final String FIRST_NAME3 = "first_name3";
  private static final String LAST_NAME3 = "last_name3";

  private CacheDataSource mCacheDataSource;

  @Before
  public void setUp() throws Exception {
    mCacheDataSource = new CacheDataSource();

  }

  @Test
  public void addPerson_getPerson() throws Exception {
    final Person person1 = Person.FACTORY.creator.create(0, FIRST_NAME1, LAST_NAME1);
    mCacheDataSource.addPerson(person1);
    mCacheDataSource.getPerson(0L).subscribe(person -> {
      assertThat(person1, is(person));
    });
  }

  @Test
  public void addPerson_addPerson() throws Exception {
    final Person person1 = Person.FACTORY.creator.create(0, FIRST_NAME1, LAST_NAME1);
    mCacheDataSource.addPerson(person1);
    mCacheDataSource.addPerson(person1).subscribe(new Subscriber<Person>() {
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
    mCacheDataSource.addPerson(person1);
    mCacheDataSource.addPerson(person2);
    mCacheDataSource.addPerson(person3);
    Observable<List<Person>> persons = mCacheDataSource.getPersons(false);
    persons.subscribe(personList -> {
      assertEquals(3, personList.size());
      assertTrue(personList.contains(person1));
      assertTrue(personList.contains(person2));
      assertTrue(personList.contains(person3));
    });
  }
}