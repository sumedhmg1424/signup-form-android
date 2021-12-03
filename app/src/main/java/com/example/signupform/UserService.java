package com.example.signupform;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("register/")
    Call<SignUpResponse> registerUsers(@Body SignUpRequest signUpRequest);


}
