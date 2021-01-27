package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    Button SearchButton;
    TextView Search;
    ArrayList<PublicEventSearch> list;
    DatabaseReference databaseReference;
    private static final String TAG = "Search";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchButton=(Button)findViewById(R.id.searchbutton);
        Search=findViewById(R.id.search);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventsToSearch;
                eventsToSearch = Search.getText().toString();
                if (!eventsToSearch.equals(""))//Chris - Check for empty Input
                {
                    Intent ResultPage = new Intent(Search.this, SearchResult.class);
                    ResultPage.putExtra("SearchEvent", eventsToSearch);
                    startActivity(ResultPage);
                }
                else {
                    Log.v(TAG, "Search Bar is empty");
                    Toast.makeText(Search.this, "Search Bar is empty", Toast.LENGTH_SHORT).show();
                    return;

                }
            }
        });
    }
}