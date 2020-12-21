package com.example.fariscare;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("v2/key-value-stores/yaPbKe9e5Et61bl7W/records/LATEST?disableRedirect=true")
    Call<Post> getPosts();
}
