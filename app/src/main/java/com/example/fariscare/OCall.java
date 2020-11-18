package com.example.fariscare;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.List;

public class OCall extends AppCompatActivity {

    RecyclerView contactsRecycler;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    SinchClient sinchClient;
    Call call;
    ArrayList<Member> memberArrayList;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_call);
        contactsRecycler=(RecyclerView)findViewById(R.id.contactRecyclerView);
        contactsRecycler.setHasFixedSize(true);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(this));

        memberArrayList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference().child("Member");
        auth=FirebaseAuth.getInstance();
        firebaseUser =auth.getCurrentUser();


        sinchClient= Sinch.getSinchClientBuilder().context(this)
                .userId(firebaseUser.getUid())
                .applicationKey("")
                .applicationSecret("").environmentHost("").build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();


        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener(){

        });
        sinchClient.start();

        fetchAllUsers();
    }

    private void fetchAllUsers(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberArrayList.clear();
                for (DataSnapshot des:dataSnapshot.getChildren()){
                    Member member=des.getValue(Member.class);

                    memberArrayList.add(member);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private class SinchCallListener implements CallListener{

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(getApplicationContext(),"Ring ring",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getApplicationContext(),"Call connected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEnded(Call endedcall) {
            Toast.makeText(getApplicationContext(),"Call ended",Toast.LENGTH_SHORT).show();
            call=null;
            endedcall.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }


    private class SinchCallClientListener implements CallClientListener{

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {

        }
    }
}