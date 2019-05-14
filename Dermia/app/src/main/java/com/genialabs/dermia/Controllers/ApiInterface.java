package com.genialabs.dermia.Controllers;

import com.genialabs.dermia.Models.Prediction;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Android on 2/17/2018.
 */



public interface ApiInterface {
    @Headers({"Connection: close", "Accept-Encoding: identity"})
    @Multipart
    @POST("predict/")
    Call<Prediction> uploadImage(@Part("ip") String token, @Part("id_user") int userId,
                                 @Part("mobile") String mobile, @Part("file") RequestBody name , @Part MultipartBody.Part file);


}
