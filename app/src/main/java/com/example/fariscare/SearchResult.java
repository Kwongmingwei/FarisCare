package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.fariscare.Adapters.RecyclerViewAdapterSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {
    ArrayList <PublicEventSearch> list;
    PublicEventSearch PES;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");

        Bundle bundle = getIntent().getExtras();
        String search=bundle.getString("SearchEvent");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("eventName").getValue().toString().equals(search)) {
                        list.add(new PublicEventSearch(snapshot.child("eventName").getValue().toString(), snapshot.child("eventDesc").getValue().toString(), snapshot.child("eventDate").getValue().toString(), "Public"));

                    }
                }

                recyclerView = findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(SearchResult.this);
                mAdapter = new RecyclerViewAdapterSearch(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
        

    public static void Fliterlist(ArrayList <PublicEventSearch> list) {
        String search="Tai Chi with the community";
        int x=0;
        for(int i=0;i<list.size();i++);
        {
            if(list.get(x).geteventName()!=search){
                list.remove(x);
            }
        }
    };
}