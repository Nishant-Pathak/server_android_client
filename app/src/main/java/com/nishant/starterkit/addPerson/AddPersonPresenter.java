package com.nishant.starterkit.addPerson;

import android.support.annotation.NonNull;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.model.Person;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AddPersonPresenter implements AddPersonContract.Presenter {

  private final PersonRepository mPersonRepository;

  private AddPersonContract.View mView;

  public AddPersonPresenter(PersonRepository mPersonRepository) {
    this.mPersonRepository = mPersonRepository;
  }

  @Override
  public void savePerson(@NonNull String firstName, @NonNull String lastName) {
    Observable<Person> personObservable = mPersonRepository.addPerson(new Person() {
      @Override
      public long _id() {
        return 0;
      }

      @NonNull
      @Override
      public String first_name() {
        return firstName;
      }

      @NonNull
      @Override
      public String last_name() {
        return lastName;
      }
    });

    personObservable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError(throwable -> {
        Timber.e(throwable);
      })
      .subscribe(person -> {
        if(mView != null) {
          mView.onSuccessfullyAdded();
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
