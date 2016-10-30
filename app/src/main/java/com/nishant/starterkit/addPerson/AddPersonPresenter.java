package com.nishant.starterkit.addPerson;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.model.Person;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddPersonPresenter implements AddPersonContract.Presenter {

  private final PersonRepository mPersonRepository;

  private AddPersonContract.View mView;

  public AddPersonPresenter(PersonRepository mPersonRepository) {
    this.mPersonRepository = mPersonRepository;
  }

  @Override
  public void savePerson(@NonNull String firstName, @NonNull String lastName) {
    Observable<Person> personObservable =
      mPersonRepository.addPerson(Person.FACTORY.creator.create(0, firstName, lastName));

    personObservable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(person -> {
        if(mView != null) {
          mView.onSuccessfullyAdded();
        }
      }, throwable -> {
        if(mView != null) {
          mView.failedToAdd(throwable.getMessage());
        }
      });
  }

  @Override
  public void attachView(AddPersonContract.View view) {
    mView = view;
  }

  @Override
  public void detach() {
    mView = null;
  }
}
