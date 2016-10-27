package com.nishant.starterkit.injection.module;

import com.nishant.starterkit.BuildConfig;
import com.nishant.starterkit.SmartHomeService;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
  @Provides
  SmartHomeService provideSmartHomeService() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client =
      new OkHttpClient
        .Builder()
        .addInterceptor(interceptor)
        .build();

    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(BuildConfig.API_HOST + ":" + BuildConfig.API_PORT)
      .addConverterFactory(GsonConverterFactory.create())
      .client(client)
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .build();
    return retrofit.create(SmartHomeService.class);
  }

}
