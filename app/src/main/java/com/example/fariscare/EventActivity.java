package com.example.fariscare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.Adapters.EventItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    private static final String TAG="Recycler View ItemList";
    private ArrayList<EventItem> mItemList; //Keith
    private RecyclerView mRecyclerView; //Contain recycler view recreated in XML layout
    private EventAdapter mAdapter; //Bridge between Arraylist and recyclerview
    private RecyclerView.LayoutManager mLayoutManager; //Responsible for aligning items in Arraylist
    FirebaseAuth Auth;
    SharedPreferences Auto_login;
    FirebaseUser user;
    String uid;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    TextView a,b,c,d;
    Button btn;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createItemList();
        buildRecyclerView();
        a=(TextView)findViewById(R.id.eventName);
        b=(TextView)findViewById(R.id.eventDate);
        c=(TextView)findViewById(R.id.eventType);
        d=(TextView)findViewById(R.id.eventDesc);
        btn=(Button)findViewById(R.id.joinBtn);

        ref = FirebaseDatabase.getInstance().getReference().child("Member").child("1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String eventName = dataSnapshot.child("eventName").getValue().toString();
                String eventDate = dataSnapshot.child("eventDate").getValue().toString();
                String eventType = dataSnapshot.child("eventType").getValue().toString();
                String eventDesc = dataSnapshot.child("eventDesc").getValue().toString();
                a.setText(eventName);
                b.setText(eventDate);
                c.setText(eventType);
                d.setText(eventDesc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        createItemList();
        buildRecyclerView();
    }

    public void createItemList() { //Replace code here with firebase code
        //mItemList.add(new EventItem(R.drawable.ic_test_img3, "Line 5", "Line 6"));
        //mItemList.add(new EventItem(TextView((R.id.eventName)));
        }

        public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EventAdapter(mItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        }
}
