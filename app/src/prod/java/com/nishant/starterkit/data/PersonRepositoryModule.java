package com.nishant.starterkit.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nishant.starterkit.data.cache.CacheDataSource;
import com.nishant.starterkit.data.local.LocalDataSource;
import com.nishant.starterkit.data.local.PersonOpenHelper;
import com.nishant.starterkit.data.remote.RemoteDataSource;
import com.nishant.starterkit.injection.annotation.Cache;
import com.nishant.starterkit.injection.annotation.Local;
import com.nishant.starterkit.injection.annotation.Remote;

import dagger.Module;
import dagger.Provides;

@Module
public class  PersonRepositoryModule {

  @NonNull
  private final Application mApplication;

  public PersonRepositoryModule(@NonNull Application mApplication) {
    this.mApplication = mApplication;
  }

  @Provides
  PersonOpenHelper providePersonOpenHelper() {
    return new PersonOpenHelper(mApplication.getApplicationContext());
  }

  @Provides
  @Local
  DataSource providesLocalDataSource(PersonOpenHelper helper) {
    return new LocalDataSource(helper);
  }

  @Provides
  @Remote
  DataSource providesRemoteDataSource() {
    return new RemoteDataSource();
  }

  @Provides
  @Cache
  DataSource providesCacheDataSource() {
    return new CacheDataSource();
  }
}
