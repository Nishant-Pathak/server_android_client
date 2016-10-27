package com.nishant.starterkit.login;

import android.content.SharedPreferences;

import com.nishant.starterkit.SmartHomeService;
import com.nishant.starterkit.data.model.Login;
import com.nishant.starterkit.injection.annotation.Local;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestObserver;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

  @Mock
  private SmartHomeService mSmartHomeService;

  @Mock
  private SharedPreferences mSharedPreferences;

  @Mock
  private LoginContract.View mLoginView;

  private String cid;
  private String uid;
  private String password;
  private String networkId;
  private String macId;

  private LoginPresenter mLoginPresenter;

  @Captor
  private ArgumentCaptor<SmartHomeService> mSmartHomeServiceArgumentCaptor;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mLoginPresenter = new LoginPresenter(mSmartHomeService, mSharedPreferences);
    mLoginPresenter.attachView(mLoginView);
    RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
      @Override
      public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
      }
    });

    Observable<Login> loginObservable = Observable.just(new Login());
    when(mSmartHomeService
      .login(
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class))).thenReturn(loginObservable);
  }

  @Test
  public void showProgressWhenLoginStarts() throws Exception {
    cid = "cid";
    uid = "uid";
    password = "password";
    networkId = "networkId";
    macId = "macId";


    mLoginPresenter.attemptLogin(cid, uid, password, networkId, macId);

    TestSubscriber<Login> loginTestSubscriber = new TestSubscriber<>();
    verify(mSmartHomeService)
      .login(
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class),
        any(RequestBody.class)
        ).subscribe(loginTestSubscriber);
    loginTestSubscriber.assertNoErrors();
    verify(mLoginView).showProgress(true);
  }

  @After
  public void tearDown() throws Exception {
    RxAndroidPlugins.getInstance().reset();

  }
}