package com.nishant.starterkit.person;

import com.nishant.starterkit.data.PersonRepository;
import com.nishant.starterkit.data.model.Person;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PersonsPresenter implements PersonsContract.Presenter {

  private final PersonRepository mPersonRepository;

  private PersonsContract.View mView;

  public PersonsPresenter(PersonRepository mPersonRepository) {
    this.mPersonRepository = mPersonRepository;
  }

  @Override
  public void getPersons(boolean forced) {
    Observable<List<Person>> personObservable = mPersonRepository.getPersons(forced);
    personObservable
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeOn(Schedulers.io())
      .subscribe(persons -> {
        if (mView != null) {
          mView.showPersons(persons);
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
