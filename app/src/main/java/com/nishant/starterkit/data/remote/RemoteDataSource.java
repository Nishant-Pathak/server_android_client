package com.nishant.starterkit.data.remote;

import android.support.annotation.NonNull;

import com.nishant.person.Empty;
import com.nishant.person.PPerson;
import com.nishant.person.PPersonList;
import com.nishant.person.PersonServiceGrpc;
import com.nishant.starterkit.BuildConfig;
import com.nishant.starterkit.data.DataSource;
import com.nishant.starterkit.data.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import rx.Observable;

@Singleton
public class RemoteDataSource implements DataSource {

  ManagedChannel mManagedChannel;
  PersonServiceGrpc.PersonServiceBlockingStub mBlockingStub;

  public RemoteDataSource() {
    mManagedChannel =
      ManagedChannelBuilder
        .forAddress(BuildConfig.API_HOST, BuildConfig.API_PORT)
        .usePlaintext(true)
        .build();

     mBlockingStub = PersonServiceGrpc.newBlockingStub(mManagedChannel);

  }

  @Override
  public Observable<Person> getPerson(@NonNull Long personId) {
    return Observable.create(subscriber -> {
      PPerson pPerson = PPerson.newBuilder().setId(personId).build();
      try {
        PPersonList reply = mBlockingStub.getPerson(pPerson);
        if (reply.getPpersonList().size() != 1) {
          subscriber.onError(new RuntimeException("person not found"));
        } else {
          Person person = transform(reply.getPperson(0));
          subscriber.onNext(person);
        }
      } catch (Exception ex) {
        subscriber.onError(ex);
      }
      subscriber.onCompleted();
    });
  }

  private Person transform(PPerson pPerson) {
    return Person.FACTORY.creator.create(pPerson.getId(), pPerson.getFirstName(), pPerson.getLastName());
  }

  @Override
  public Observable<List<Person>> getPersons(boolean forced) {
    return Observable.create(subscriber -> {
      Empty empty = Empty.getDefaultInstance();
      try {
        PPersonList allPerson = mBlockingStub.getAllPerson(empty);
        allPerson.getPpersonList();
        List<Person> personList = new ArrayList<>(allPerson.getPpersonCount());
        for(int i = 0; i < allPerson.getPpersonCount(); i++) {
          personList.add(transform(allPerson.getPperson(i)));
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
      PPerson pPerson = PPerson.newBuilder().setFirstName(person.first_name()).setLastName(person.last_name()).build();
      try {
        PPerson response = mBlockingStub.addPerson(pPerson);
        subscriber.onNext(transform(response));
      } catch (Exception ex) {
        subscriber.onError(ex);
      }
      subscriber.onCompleted();
    });
  }
}
