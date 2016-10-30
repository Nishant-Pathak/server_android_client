package com.nishant.starterkit.person;

import com.nishant.starterkit.BasePresenter;
import com.nishant.starterkit.BaseView;
import com.nishant.starterkit.data.model.Person;

import java.util.List;

public class PersonsContract {
  interface Presenter extends BasePresenter<View> {
    void getPersons(boolean forced);
  }

  interface View extends BaseView {
    void showPersons(List<Person> persons);

    void showError(String message);
  }
}
