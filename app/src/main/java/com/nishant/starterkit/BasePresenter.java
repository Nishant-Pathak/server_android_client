package com.nishant.starterkit;

public interface BasePresenter<T> {

  void attachView(T view);

  void detach();
}
