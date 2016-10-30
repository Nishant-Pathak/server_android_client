package com.nishant.starterkit.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.nishant.person.Empty;
import com.nishant.person.PPerson;
import com.nishant.person.PersonServiceGrpc;
import com.nishant.starterkit.BuildConfig;
import com.nishant.starterkit.data.cache.CacheDataSource;
import com.nishant.starterkit.data.local.LocalDataSource;
import com.nishant.starterkit.data.local.PersonOpenHelper;
import com.nishant.starterkit.data.remote.RemoteDataSource;
import com.nishant.starterkit.injection.annotation.Cache;
import com.nishant.starterkit.injection.annotation.Local;
import com.nishant.starterkit.injection.annotation.Remote;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

@Module
@Singleton
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
  @Local
  @Singleton
  DataSource providesLocalDataSource(PersonOpenHelper helper) {
    return new LocalDataSource(helper);
  }

  @Provides
  @Remote
  @Singleton
  DataSource providesRemoteDataSource() {
    ManagedChannel managedChannel =
      ManagedChannelBuilder
        .forAddress(BuildConfig.API_HOST, BuildConfig.API_PORT)
        .usePlaintext(true)
        .build();
    return new RemoteDataSource(PersonServiceGrpc.newFutureStub(managedChannel));
  }

  @Provides
  @Cache
  @Singleton
  DataSource providesCacheDataSource() {
    return new CacheDataSource();
  }
}
