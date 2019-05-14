package com.genialabs.dermia.Controllers;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //"http://ec2-3-17-231-221.us-east-2.compute.amazonaws.com:8000/";  DJANGO
    //"http://ec2-3-14-18-0.us-east-2.compute.amazonaws.com:7000/"; FLASK

    private static final String BASE_URL = "http://ec2-3-14-94-226.us-east-2.compute.amazonaws.com:7000/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);



            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    //.addInterceptor(loggingInterceptor)
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }
        return retrofit;
    }
}