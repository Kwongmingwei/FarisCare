package com.example.fariscare;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class CallScreen extends AppCompatActivity {
    boolean mutetoggle;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    SinchClient sinchClient;
    Call call;
    DatabaseReference reference;
    Button hangup;
    ImageButton mute, speaker, mic;
    TextView callinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        mutetoggle = true;
        hangup = (Button)findViewById(R.id.hangup);
        speaker = (ImageButton)findViewById(R.id.speaker);
        callinfo = (TextView)findViewById(R.id.callinfo);
        reference= FirebaseDatabase.getInstance().getReference().child("Member");
        auth=FirebaseAuth.getInstance();
        firebaseUser =auth.getCurrentUser();
        CallClient callClient = sinchClient.getCallClient();
        Call call = callClient.callUser("<remote user id>");

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId("")
                .applicationKey("")
                .applicationSecret("")
                .environmentHost("")
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        if (call == null) {
            call = sinchClient.getCallClient().callUser("recipientId");
            call.addCallListener(new SinchCallListener());
        } else {
            call.hangup();
        }
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isSpeakerphoneOn()){
                    audioManager.setSpeakerphoneOn(false);
                }
                else{
                    audioManager.setSpeakerphoneOn(true);
                }

            }
        });

    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            callinfo.setText("Ringing...");
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getApplicationContext(),"Call connected",Toast.LENGTH_SHORT).show();
            callinfo.setText("Call in progress");
        }

        @Override
        public void onCallEnded(Call endedcall) {
            Toast.makeText(getApplicationContext(),"Call ended",Toast.LENGTH_SHORT).show();
            callinfo.setText("Call ended");
            call=null;
            endedcall.hangup();
            Intent end = new Intent(CallScreen.this, OCall.class);
            startActivity(end);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

}
