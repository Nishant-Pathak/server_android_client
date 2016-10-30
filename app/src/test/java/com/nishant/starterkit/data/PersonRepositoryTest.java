package com.nishant.starterkit.data;

import com.nishant.starterkit.data.cache.CacheDataSource;
import com.nishant.starterkit.data.local.LocalDataSource;
import com.nishant.starterkit.data.model.Person;
import com.nishant.starterkit.data.remote.RemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import rx.Observable;
import rx.Subscription;
import rx.observers.TestObserver;
import rx.observers.TestSubscriber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonRepositoryTest {

  private PersonRepository mPersonRepository;
  private DataSource mRemoteDataSource;
  private DataSource mLocalDataSource;
  private DataSource mCacheDataSource;
  private static final Person PERSON = Person.FACTORY.creator.create(0, "first_name", "last_name");


  @Before
  public void setUp() throws Exception {
    mCacheDataSource = mock(CacheDataSource.class);
    mLocalDataSource = mock(LocalDataSource.class);
    mRemoteDataSource = mock(RemoteDataSource.class);
    mPersonRepository = new PersonRepository(mLocalDataSource, mRemoteDataSource, mCacheDataSource);
  }

  @Test
  public void addPerson() throws Exception {
    TestSubscriber<Person> testSubscriber = new TestSubscriber<>();

    when(mRemoteDataSource.addPerson(PERSON)).thenReturn(Observable.just(PERSON));
    // TODO verify local and cache person to be called???
    mPersonRepository.addPerson(PERSON)
      .subscribe(testSubscriber);
    testSubscriber.assertNoErrors();
    testSubscriber.assertReceivedOnNext(Collections.singletonList(PERSON));
  }

  @Test
  public void getPersonFromCache() throws Exception {
    TestSubscriber<Person> testSubscriber = new TestSubscriber<>();
    when(mRemoteDataSource.getPerson(0L)).thenReturn(Observable.empty());
    when(mLocalDataSource.getPerson(0L)).thenReturn(Observable.empty());
    when(mCacheDataSource.getPerson(0L)).thenReturn(Observable.just(PERSON));

    mPersonRepository.getPerson(0L).subscribe(testSubscriber);

    testSubscriber.assertNoErrors();
    testSubscriber.assertReceivedOnNext(Collections.singletonList(PERSON));

  }

  @Test
  public void getPersonFromLocal() throws Exception {
    TestSubscriber<Person> testSubscriber = new TestSubscriber<>();
    when(mRemoteDataSource.getPerson(0L)).thenReturn(Observable.empty());
    when(mLocalDataSource.getPerson(0L)).thenReturn(Observable.just(PERSON));
    when(mCacheDataSource.getPerson(0L)).thenReturn(Observable.empty());

    mPersonRepository.getPerson(0L).subscribe(testSubscriber);

    testSubscriber.assertNoErrors();
    testSubscriber.assertReceivedOnNext(Collections.singletonList(PERSON));

  }

  @Test
  public void getPersonEmpty() throws Exception {
    TestSubscriber<Person> testSubscriber = new TestSubscriber<>();
    when(mRemoteDataSource.getPerson(0L)).thenReturn(Observable.empty());
    when(mLocalDataSource.getPerson(0L)).thenReturn(Observable.empty());
    when(mCacheDataSource.getPerson(0L)).thenReturn(Observable.empty());

    mPersonRepository
      .getPerson(0L)
      .doOnError(throwable -> {
        assertEquals(NoSuchElementException.class, throwable.getClass());
      })
      .subscribe(testSubscriber);
  }


}