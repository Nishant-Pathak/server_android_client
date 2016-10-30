package com.nishant.starterkit.addPerson;

import com.nishant.starterkit.BasePresenter;
import com.nishant.starterkit.BaseView;

public class AddPersonContract {
  interface Presenter extends BasePresenter<View> {
    void savePerson(String firstName, String lastName);
  }

  interface View extends BaseView {
    void onSuccessfullyAdded();

    void failedToAdd(String message);
  }
}
