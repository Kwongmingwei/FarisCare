package com.example.fariscare;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiPage extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_page);

        textView=findViewById(R.id.text_view_result);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1,TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.apify.com/").addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        Call<Post> call=jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()){
                    textView.setText("Code: "+response.code());
                    return;
                }

                Post posts=response.body();

                String content="";
                content+="Current active cases: "+posts.getActiveCases()+"\n";
                content+="Stable hospitalized: "+posts.getStableHospitalized()+"\n";
                content+="Current critical patients hospitalized: "+posts.getCriticalHospitalized()+"\n";
                content+="Total infected: "+posts.getInfected()+"\n";
                content+="Total deceased: "+posts.getDeceased()+"\n";
                content+="Total recovered: "+posts.getRecovered()+"\n";
                content+="Last Updated: "+posts.getLastUpdatedAtApify()+"\n\n";
                textView.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }
}