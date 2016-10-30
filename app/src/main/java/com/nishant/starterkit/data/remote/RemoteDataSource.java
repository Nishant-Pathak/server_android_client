package com.nishant.starterkit.data.remote;

import android.support.annotation.NonNull;

import com.google.common.util.concurrent.ListenableFuture;
import com.nishant.person.Empty;
import com.nishant.person.PPerson;
import com.nishant.person.PPersonList;
import com.nishant.person.PersonServiceGrpc;
import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class RemoteDataSource implements DataSource {

  private PersonServiceGrpc.PersonServiceFutureStub mFutureStub;

  public RemoteDataSource(PersonServiceGrpc.PersonServiceFutureStub blockingStub) {
    mFutureStub = blockingStub;
  }

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    return Observable.create(subscriber -> {
      PPerson pPerson = PPerson.newBuilder().setId(personId).build();
      ListenableFuture<PPersonList> reply = mFutureStub.getPerson(pPerson);
      try {
        PPersonList pPersonList = reply.get();
        if (pPersonList.getPpersonCount() != 1) {
          subscriber.onError(new RuntimeException("person not found"));
        } else {
          Person person = transform(pPersonList.getPpersonList().get(0));
          subscriber.onNext(person);
        }
      } catch (Exception ex) {
        subscriber.onError(ex);
      }
      subscriber.onCompleted();
    });
  }

  private Person transform(PPerson pPerson) {
    return
      Person
        .FACTORY
        .creator
        .create(pPerson.getId(), pPerson.getFirstName(), pPerson.getLastName());
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    return Observable.create(subscriber -> {
      Empty empty = Empty.getDefaultInstance();
      ListenableFuture<PPersonList> listListenableFuture = mFutureStub.getAllPerson(empty);
      try {
        PPersonList pPersonList = listListenableFuture.get();
        List<Person> personList = new ArrayList<>(pPersonList.getPpersonCount());
        for(int i = 0; i < pPersonList.getPpersonCount(); i++) {
          personList.add(transform(pPersonList.getPperson(i)));
        }
        subscriber.onNext(personList);
      } catch (Exception ex) {
        subscriber.onError(ex);
      }
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<Person> addPerson(@NonNull Person person) {
    return Observable.create(subscriber -> {
      PPerson pPerson =
        PPerson
          .newBuilder()
          .setFirstName(person.first_name())
          .setLastName(person.last_name())
          .build();

      ListenableFuture<PPerson> response = mFutureStub.addPerson(pPerson);
      try {
        PPerson pPersonResponse = response.get();
        subscriber.onNext(transform(pPersonResponse));
      } catch (ExecutionException | InterruptedException ex) {
        subscriber.onError(ex);
      }
      subscriber.onCompleted();
    });
  }
}
