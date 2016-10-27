package com.nishant.starterkit.login;

import android.support.annotation.NonNull;

import com.nishant.starterkit.BasePresenter;
import com.nishant.starterkit.BaseView;

public class LoginContract {
  interface View extends BaseView {

    void clearError();

    void showError(String message);

    void loginSuccess();

    void showProgress(boolean show);
  }

  interface Presenter extends BasePresenter<View> {

    void attemptLogin(
      @NonNull String cid,
      @NonNull String uid,
      @NonNull String password,
      @NonNull String networkId,
      @NonNull String macId);
  }
}
