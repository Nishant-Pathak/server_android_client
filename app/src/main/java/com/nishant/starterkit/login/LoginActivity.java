package com.nishant.starterkit.login;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.nishant.starterkit.R;
import com.nishant.starterkit.StarterApplication;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends RxAppCompatActivity implements LoginContract.View {

  LoginComponent mLoginComponent;

  @Inject
  LoginPresenter mLoginPresenter;

  // UI references.
  @BindView(R.id.cid)
  EditText mCid;

  @BindView(R.id.cid_layout)
  TextInputLayout mCidLayout;

  @BindView(R.id.password)
  EditText mPassword;

  @BindView(R.id.password_layout)
  TextInputLayout mPasswordLayout;

  @BindView(R.id.uid)
  EditText mUid;

  @BindView(R.id.uid_layout)
  TextInputLayout mUidLayout;

  @BindView(R.id.network)
  EditText mNetwork;

  @BindView(R.id.network_layout)
  TextInputLayout mNetworkLayout;

  @BindView(R.id.mac_id)
  EditText mMacId;

  @BindView(R.id.mac_id_layout)
  TextInputLayout mMacIdLayout;

  @BindView(R.id.login_progress)
  View mProgressView;

  @BindView(R.id.login_form)
  View mLoginFormView;

  @BindView(R.id.sign_in_button)
  Button mSignInButton;

  @VisibleForTesting
  LoginComponent mTestActivityComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (mTestActivityComponent != null) {
      mTestActivityComponent.inject(LoginActivity.this);
    } else {
      DaggerLoginComponent
        .builder()
        .applicationComponent(((StarterApplication)getApplication()).getComponent())
        .build().inject(LoginActivity.this);
    }
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    mLoginPresenter.attachView(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    initiateLogin();
  }

  @Override
  protected void onDestroy() {
    mLoginPresenter.detach();
    super.onDestroy();
  }
  private Observable<Boolean> createTextViewObservableWithLayoutError(
    EditText editText,
    TextInputLayout inputLayout,
    Func1<CharSequence, Boolean> func,
    String errorMessage
  ) {
    Observable<Boolean> observable =
      RxTextView
        .textChanges(editText)
        .map(func)
        .compose(bindToLifecycle());
    observable
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(isValid -> {
        inputLayout.setErrorEnabled(!isValid);
        if(!isValid) {
          inputLayout.setError(errorMessage);
        } else {
          inputLayout.setError(null);
        }
      });
    return observable;
  }

  private void initiateLogin() {
    Observable<Boolean> cidObservable =
      createTextViewObservableWithLayoutError(
        mCid,
        mCidLayout,
        charSequence -> charSequence.length() == 0 || charSequence.toString().matches(".*"),
        getString(R.string.cid_error_message));

    Observable<Boolean> uidObservable =
      createTextViewObservableWithLayoutError(
        mUid,
        mUidLayout,
        charSequence -> charSequence.length() == 0 || charSequence.toString().matches(".*"),
        getString(R.string.uid_error_message));

    Observable<Boolean> passwordObservable =
      createTextViewObservableWithLayoutError(
        mPassword,
        mPasswordLayout,
        charSequence -> charSequence.length() == 0 || charSequence.toString().matches(".*"),
        getString(R.string.password_error_message));

    Observable<Boolean> networkObservable =
      createTextViewObservableWithLayoutError(
        mNetwork,
        mNetworkLayout,
        charSequence -> charSequence.length() == 0 || charSequence.toString().matches(".*"),
        getString(R.string.network_error_message));

    Observable<Boolean> macIdObservable =
      createTextViewObservableWithLayoutError(
        mMacId,
        mMacIdLayout,
        charSequence -> charSequence.length() == 0 || charSequence.toString().matches(".*"),
        getString(R.string.mac_error_message));

    Observable.combineLatest(
      cidObservable,
      uidObservable,
      passwordObservable,
      networkObservable,
      macIdObservable,
      (cidValid, uidValid, passwordValid, networkValid, macIdValid) ->
        cidValid && uidValid && passwordValid && networkValid && macIdValid)
      .distinctUntilChanged()
      .subscribe(valid -> mSignInButton.setEnabled(valid));
  }

  @OnClick(R.id.sign_in_button)
  public void emailSignInButtonClicked() {
    mLoginPresenter.attemptLogin(
      mCid.getText().toString(),
      mUid.getText().toString(),
      mPassword.getText().toString(),
      mNetwork.getText().toString(),
      mMacId.getText().toString()
    );
  }

  @Override
  public void clearError() {
    Toast.makeText(this, "Clear error", Toast.LENGTH_LONG).show();
  }

  @Override
  public void showError(String message) {
    Toast.makeText(this, "Login failed: " + message, Toast.LENGTH_LONG).show();
  }

  @Override
  public void loginSuccess() {
    Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show();
  }

  @Override
  public void showProgress(boolean show) {
    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    mSignInButton.setEnabled(!show);
  }

  @VisibleForTesting
  public void addComponent(LoginComponent component) {
    mTestActivityComponent = component;
  }
}

