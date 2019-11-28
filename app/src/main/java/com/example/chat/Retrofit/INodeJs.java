package com.example.chat.Retrofit;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface INodeJs {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);


}
