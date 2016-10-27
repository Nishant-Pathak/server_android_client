package com.nishant.starterkit;

import com.nishant.starterkit.data.model.Login;

import okhttp3.RequestBody;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface SmartHomeService {

  @Multipart
  @Headers("Content-Type: multipart/form-data")
  @POST("login")
  Observable<Login> login(
    @Part("cid") RequestBody cid,
    @Part("uid") RequestBody uid,
    @Part("pwd") RequestBody pwd,
    @Part("nw") RequestBody nw,
    @Part("macid") RequestBody macid
  );
}
