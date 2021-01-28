package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    String uid;
    private RecyclerViewAdapterSearch mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_public_events);
        list=new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        uid=bundle.getString("User_UID");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("eventType").getValue().toString().equals("Public")) {
                        list.add(new PublicEventSearch(snapshot.child("eventID").getValue().toString(),snapshot.child("eventName").getValue().toString(), snapshot.child("eventDesc").getValue().toString(), snapshot.child("eventDate").getValue().toString(), "Public",snapshot.child("eventTime").getValue().toString(),"0"));
                    }
                }

                recyclerView = findViewById(R.id.rv1);
                layoutManager = new LinearLayoutManager(ViewPublicEvents.this);
                mAdapter = new RecyclerViewAdapterSearch(list);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new RecyclerViewAdapterSearch.OnItemClickListener() {

                    @Override
                    public void onItemClick(int position) {
                        String name=list.get(position).geteventName();
                        String member=list.get(position).getParticipants();

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("eventName").getValue().toString().equals(name)) {
                                        String [] check=snapshot.child("participants").getValue().toString().split(",");
                                        for (int i=0;i<check.length;i++)
                                        {
                                            if(check[i].equals(uid)){
                                                Toast.makeText(ViewPublicEvents.this, "You already join this event...", Toast.LENGTH_SHORT).show();
                                                Intent View=new Intent(ViewPublicEvents.this,EventHubMain.class);
                                                View.putExtra("User_UID", uid);
                                                startActivity(View);
                                                finish();
                                            }
                                        }
                                        String updateUID=member+","+uid;
                                        databaseReference.child(snapshot.child("eventID").getValue().toString()).child("participants").setValue(updateUID);
                                        list.remove(list.get(position));
                                        mAdapter.notifyDataSetChanged();
                                        break;
                                    }

                                }
                                Intent View=new Intent(ViewPublicEvents.this,EventHubMain.class);
                                View.putExtra("User_UID", uid);
                                startActivity(View);
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

}