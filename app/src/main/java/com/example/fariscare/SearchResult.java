package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.TextView;
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
    DatabaseReference databaseReference;
    TextView Nothing,Search;
    String uid;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSearch mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");
        Nothing=findViewById(R.id.Nothing);
        Search=findViewById(R.id.SearchResult);
        Bundle bundle = getIntent().getExtras();
        String search=bundle.getString("SearchEvent");
        uid=bundle.getString("User_UID");
        Search.setText("Search: "+search);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.child("eventType").getValue().toString().equals("Public")) {
                        if (snapshot.child("eventName").getValue().toString().equals(search)) {
                            list.add(new PublicEventSearch(snapshot.child("eventID").getValue().toString(),snapshot.child("eventName").getValue().toString(), snapshot.child("eventDesc").getValue().toString(), snapshot.child("eventDate").getValue().toString(), "Public",snapshot.child("eventTime").getValue().toString(),"0"));

                        }
                    }
                }
                if(list.size()!=0) {
                    Nothing.setVisibility(View.GONE);
                    recyclerView = findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(SearchResult.this);
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
                                                    Intent View=new Intent(SearchResult.this,EventHubMain.class);
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
                                    Intent View=new Intent(SearchResult.this,EventHubMain.class);
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
                else
                {
                    Nothing.setText("NO RESULT HAS BEEN FOUND....");
                }
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