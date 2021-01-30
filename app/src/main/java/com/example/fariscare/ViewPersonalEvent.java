package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fariscare.Adapters.RecyclerViewAdapterSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPersonalEvent extends AppCompatActivity {

    ArrayList <PublicEventSearch> list;
    DatabaseReference databaseReference;
    TextView Nothing,Add;
    String updateUID;
    String uid;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSearch mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal_event);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");
        Nothing=findViewById(R.id.Nothing);
        Add=findViewById(R.id.Add);
        Bundle bundle = getIntent().getExtras();
        updateUID="0";
        uid=bundle.getString("User_UID");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String [] check=snapshot.child("participants").getValue().toString().split(",");
                    for (int i=0;i<check.length;i++)
                    {
                        if(check[i].equals(uid)){
                            list.add(new PublicEventSearch(snapshot.child("eventID").getValue().toString(),snapshot.child("eventName").getValue().toString(), snapshot.child("eventDesc").getValue().toString(), snapshot.child("eventDate").getValue().toString(), snapshot.child("eventType").getValue().toString(),snapshot.child("eventTime").getValue().toString(),"0"));
                        }
                    }
                }
                if(list.size()!=0) {
                    Nothing.setVisibility(View.GONE);
                    recyclerView = findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(ViewPersonalEvent.this);
                    mAdapter = new RecyclerViewAdapterSearch(list);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mAdapter);

                }
                else
                {
                    Intent View=new Intent(ViewPersonalEvent.this,ViewPublicEvents.class);
                    View.putExtra("User_UID", uid);
                    startActivity(View);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent View=new Intent(ViewPersonalEvent.this,AddEvent.class);
                View.putExtra("User_UID", uid);
                startActivity(View);
                finish();
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