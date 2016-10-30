package com.nishant.starterkit.person;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.model.Person;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PersonsPresenter implements PersonsContract.Presenter {

  private final PersonRepository mPersonRepository;

  private PersonsContract.View mView;

  public PersonsPresenter(PersonRepository mPersonRepository) {
    this.mPersonRepository = mPersonRepository;
  }

  @Override
  public void getPersons(boolean forced) {
    mPersonRepository.getPersons(forced)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeOn(Schedulers.io())
      .subscribe(new Subscriber<List<Person>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
          if (mView != null) {
            mView.showError(e.getMessage());
          }

        }

        @Override
        public void onNext(List<Person> persons) {
          if (mView != null) {
            mView.showPersons(persons);
          }
        }
      });
  }

  @Override
  public void attachView(PersonsContract.View view) {
    mView = view;
    getPersons(false);
  }

  @Override
  public void detach() {
    mView = null;
  }
}
