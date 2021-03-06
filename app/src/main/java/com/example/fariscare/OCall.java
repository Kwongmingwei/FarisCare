package com.example.fariscare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.Adapters.AllUsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.ArrayList;
import java.util.List;

public class OCall extends AppCompatActivity {

    final static String TAG = "OCall.java";

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
        contactsRecycler = (RecyclerView) findViewById(R.id.contactRecyclerView);
        contactsRecycler.setHasFixedSize(true);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(this));

        memberArrayList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Member");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        sinchClient = Sinch.getSinchClientBuilder().context(this)
                .userId(firebaseUser.getUid())
                .applicationKey("63cc94ee-d4f8-4805-8938-6067e98fc8fa")
                .applicationSecret("t1FsM6t2k0+Pq8hKPzW3sQ==").environmentHost("clientapi.sinch.com").build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();


        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener() {
            public void onClientStarted(SinchClient client) {
            }

            public void onClientStopped(SinchClient client) {
            }

            public void onClientFailed(SinchClient client, SinchError error) {
            }

            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
            }

            public void onLogMessage(int level, String area, String message) {
            }

        });
        sinchClient.start();

        fetchAllUsers();
    }

    private void fetchAllUsers() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memberArrayList.clear();
                for (DataSnapshot des : dataSnapshot.getChildren()) {
                    Member member = des.getValue(Member.class);
                    memberArrayList.add(member);
                }


                AllUsersAdapter adapter = new AllUsersAdapter(OCall.this, memberArrayList);
                int spanCount = pxToDp(getScreenWidth()) / 120;
                Log.v(TAG, "span count is " + spanCount);
                GridLayoutManager gLayoutManager = new GridLayoutManager(OCall.this, spanCount);
                contactsRecycler.setLayoutManager(gLayoutManager);
                contactsRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    // Get current screen width in px
    public static int getScreenWidth() {
        Log.v(TAG, "Screen width in px : " + (Resources.getSystem().getDisplayMetrics().widthPixels));
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call call) {
            Toast.makeText(getApplicationContext(), "Ring ring", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEstablished(Call call) {
            Toast.makeText(getApplicationContext(), "Call connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEnded(Call endedcall) {
            Toast.makeText(getApplicationContext(), "Call ended", Toast.LENGTH_SHORT).show();
            call = null;
            endedcall.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }


    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, final Call incomingcall) {

            AlertDialog alertDialog = new AlertDialog.Builder(OCall.this).create();
            alertDialog.setTitle("Calling...");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call = incomingcall;
                    dialog.dismiss();
                    call.hangup();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pick up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    call = incomingcall;
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                    Toast.makeText(getApplicationContext(), "Call is answered", Toast.LENGTH_LONG).show();
                }
            });

            alertDialog.show();
        }
    }

    public void callUser(Member member) {
        if (call == null) {
            Log.v(TAG, "CALL IS NULL");
            Log.v(TAG, "USERID of calling person is" + member.getUserID());
            call = sinchClient.getCallClient().callUser(member.getUserID());
            call.addCallListener(new SinchCallListener());

            openCallerDialog(call,member);
        }
    }
    private void openCallerDialog(final Call call, Member member) {
        //Faris Call UI
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        AlertDialog alertDialogCall=new AlertDialog.Builder(OCall.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_call_screen, null);
        alertDialogCall.setView(view);
        Button hangup = (Button)view.findViewById(R.id.hangup);
        ImageButton speaker = (ImageButton)view.findViewById(R.id.speaker);
        TextView name = (TextView)view.findViewById(R.id.name);
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCall.dismiss();
                call.hangup();
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
        name.setText(member.getName());
        alertDialogCall.show();
    }
}