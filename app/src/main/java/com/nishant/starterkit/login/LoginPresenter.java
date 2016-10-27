package com.nishant.starterkit.login;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.nishant.starterkit.BaseView;
import com.nishant.starterkit.SmartHomeService;
import com.nishant.starterkit.data.model.Login;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

  private LoginContract.View mLoginView;

  private Subscription mLoginSubscription;

  SmartHomeService mSmartHomeService;

  SharedPreferences mSharedPreferences;

  public LoginPresenter(
    @NonNull SmartHomeService mSmartHomeService,
    @NonNull SharedPreferences mSharedPreferences
  ) {
    this.mSmartHomeService = mSmartHomeService;
    this.mSharedPreferences = mSharedPreferences;
  }

  @Override
  public void attachView(LoginContract.View view) {
    mLoginView = view;
  }

  @Override
  public void detach() {
    mLoginView = null;
    if (mLoginSubscription != null) {
      mLoginSubscription.unsubscribe();
    }
  }

  private RequestBody createPayload(String payload) {
    return RequestBody.create(MediaType.parse("text/plain"), payload);
  }

  @Override
  public void attemptLogin(
    @NonNull String cid,
    @NonNull String uid,
    @NonNull String password,
    @NonNull String networkId,
    @NonNull String macId
  ) {
    mLoginView.showProgress(true);
    Observable<Login> loginObservable =
      mSmartHomeService.login(
        createPayload(cid),
        createPayload(uid),
        createPayload(password),
        createPayload(networkId),
        createPayload(macId)
      );

    mLoginSubscription = loginObservable
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new LoginSubscriber());
  }

  private void loginSuccess(@NonNull Login login) {
    if (mLoginView != null) {
      mLoginView.showProgress(false);
      mLoginView.loginSuccess();
    }
  }

  private void loginFailure(@NonNull Throwable error) {
    if (mLoginView != null) {
      mLoginView.showProgress(false);
      mLoginView.showError(error.getMessage());
    }
  }

  private class LoginSubscriber extends Subscriber<Login> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
      loginFailure(e);
    }

    @Override
    public void onNext(Login login) {
      loginSuccess(login);
    }
  }
}
