package com.nishant.starterkit.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nishant.starterkit.data.cache.CacheDataSource;
import com.nishant.starterkit.data.local.LocalDataSource;
import com.nishant.starterkit.data.local.PersonOpenHelper;
import com.nishant.starterkit.data.remote.FakeRemoteDataSource;
import com.nishant.starterkit.injection.annotation.Cache;
import com.nishant.starterkit.injection.annotation.Local;
import com.nishant.starterkit.injection.annotation.Remote;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class  PersonRepositoryModule {

  @NonNull
  private final Application mApplication;

  public PersonRepositoryModule(@NonNull Application mApplication) {
    this.mApplication = mApplication;
  }

  @Provides
  @Singleton
  PersonOpenHelper providePersonOpenHelper() {
    return new PersonOpenHelper(mApplication.getApplicationContext());
  }

  @Provides
  @Singleton
  @Local
  DataSource providesLocalDataSource(PersonOpenHelper helper) {
    return new LocalDataSource(helper);
  }

  @Provides
  @Singleton
  @Remote
  DataSource providesRemoteDataSource() {
    return new FakeRemoteDataSource();
  }

  @Provides
  @Singleton
  @Cache
  DataSource providesCacheDataSource() {
    return new CacheDataSource();
  }
}
