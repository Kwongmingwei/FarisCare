package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fariscare.Adapters.RecyclerViewAdapterSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPublicEvents extends AppCompatActivity {
    ArrayList<PublicEventSearch> list;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_public_events);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("eventType").getValue().toString().equals("Public")) {
                        list.add(new PublicEventSearch(snapshot.child("eventName").getValue().toString(), snapshot.child("eventDesc").getValue().toString(), snapshot.child("eventDate").getValue().toString(), "Public",snapshot.child("eventTime").getValue().toString(),"0"));


                    }
                }

                recyclerView = findViewById(R.id.rv1);
                layoutManager = new LinearLayoutManager(ViewPublicEvents.this);
                mAdapter = new RecyclerViewAdapterSearch(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}