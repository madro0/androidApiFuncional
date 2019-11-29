package com.example.chat.Retrofit;

/*import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit intance;

    public static  Retrofit getInstance(){
        if (intance == null){
            intance = new Retrofit.Builder()
                    .baseUrl("https://192.168.1.4:3000/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return intance;
    }

}
*/

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit intance;
    private static String BASE_URL = "http://192.168.43.103:3000";

    public static  Retrofit getInstance(){
        if (intance == null){
            intance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return intance;
    }

}