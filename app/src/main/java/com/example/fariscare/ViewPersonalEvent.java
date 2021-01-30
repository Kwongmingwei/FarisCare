package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fariscare.Adapters.RecyclerViewAdapterSearch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPersonalEvent extends AppCompatActivity {

    ArrayList<PublicEventSearch> list;
    DatabaseReference databaseReference;
    TextView Add;
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
        Add=findViewById(R.id.Add);
        Bundle bundle = getIntent().getExtras();
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
                    if(list.size()!=0) {
                        recyclerView = findViewById(R.id.rv);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(ViewPersonalEvent.this);
                        mAdapter = new RecyclerViewAdapterSearch(list);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(mAdapter);}
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
                                                    String[] array2 = Arrays.copyOfRange(check, 0, i);
                                                    String string=String.valueOf(array2);
                                                    databaseReference.child(snapshot.child("eventID").getValue().toString()).child("participants").setValue(string);
                                                    list.remove(list.get(position));
                                                    mAdapter.notifyDataSetChanged();
                                                    Intent View=new Intent(ViewPersonalEvent.this,EventHubMain.class);
                                                    View.putExtra("User_UID", uid);
                                                    startActivity(View);
                                                    finish();
                                                }
                                            }

                                            break;
                                        }

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}