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

    private TextView cActive;
    private TextView stable;
    private TextView crictical;
    private TextView totalInfected;
    private TextView totalDeceased;
    private TextView totalRecovered;
    private TextView lastUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_page);



        cActive=findViewById(R.id.cActiveNo);
        stable=findViewById(R.id.stableNo);
        crictical=findViewById(R.id.cricticalNo);
        totalInfected=findViewById(R.id.totalInfectedNo);
        totalDeceased=findViewById(R.id.totalDeceasedNo);
        totalRecovered=findViewById(R.id.totalRecoveredNo);
        lastUpdated=findViewById(R.id.lastUpdatedNo);
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
                    lastUpdated.setText("Code: "+response.code());
                    return;
                }

                Post posts=response.body();

                cActive.setText(posts.getActiveCases());
                stable.setText(posts.getStableHospitalized());
                crictical.setText(posts.getCriticalHospitalized());
                totalInfected.setText(posts.getInfected());
                totalDeceased.setText(posts.getDeceased());
                totalRecovered.setText(posts.getRecovered());
                lastUpdated.setText(posts.getLastUpdatedAtApify());

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                lastUpdated.setText(t.getMessage());
            }
        });
    }
}