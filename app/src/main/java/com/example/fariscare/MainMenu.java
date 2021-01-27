package com.example.fariscare;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.util.List;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MainMenu extends AppCompatActivity {
    Button emergencyButton;
    Button socialButton;
    Button apiButton;
    Button eventButton;
    String eContact,uid;
    TextView address,name,dob;
    DatabaseReference databaseReference;
    ImageView ic,call;
    SinchClient sinchClient;
    Call calls;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActivityCompat.requestPermissions(MainMenu.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        apiButton=(Button)findViewById(R.id.groceries);
        socialButton=(Button)findViewById(R.id.social);
        eventButton = (Button) findViewById(R.id.event);
        eContact = getIntent().getStringExtra("emergency");
        ic=findViewById(R.id.imageView9);
        name=findViewById(R.id.NameHome);
        dob=findViewById(R.id.DOBHome);
        address=findViewById(R.id.AddressHome);
        call=findViewById(R.id.imageView4);
        Bundle bundle = getIntent().getExtras();
        uid=bundle.getString("User_UID");
        Log.v("Menu","Emergency contact: "+eContact);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("name").getValue().toString();
                String birth = dataSnapshot.child("dob").getValue().toString();
                String address1 = dataSnapshot.child("address").getValue().toString();
                name.setText(n);
                address.setText(address1);
                dob.setText(birth);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference=FirebaseDatabase.getInstance().getReference().child("Member");
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();

        Log.v("sinchuid",firebaseUser.getUid());

        sinchClient= Sinch.getSinchClientBuilder().context(this)
                .userId(firebaseUser.getUid())
                .applicationKey("63cc94ee-d4f8-4805-8938-6067e98fc8fa")
                .applicationSecret("t1FsM6t2k0+Pq8hKPzW3sQ==").environmentHost("clientapi.sinch.com").build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.setSupportActiveConnectionInBackground(true);


        sinchClient.getCallClient().addCallClientListener(new MainMenu.SinchCallClientListener(){
            public void onClientStarted(SinchClient client) { }

            public void onClientStopped(SinchClient client) { }

            public void onClientFailed(SinchClient client, SinchError error) { }

            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) { }

            public void onLogMessage(int level, String area, String message) { }

        });
        sinchClient.start();
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent social = new Intent(MainMenu.this, OCall.class);
                startActivity(social);
            }
            });

        apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent covidApi = new Intent(MainMenu.this, ApiPage.class);
                startActivity(covidApi);
            }
        });

        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent social=new Intent(MainMenu.this,OCall.class);
                social.putExtra("uid", uid);
                startActivity(social);

            }
        });

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent event=new Intent(MainMenu.this,AddEvent.class);
                startActivity(event);

            }
        });
        ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile=new Intent(MainMenu.this,ProfilePage.class);
                profile.putExtra("uid", uid);
                startActivity(profile);
            }
        });

        emergencyButton = (Button) findViewById(R.id.emergency);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eContact==NULL){
                    Toast.makeText(getApplicationContext(),"No emergency contact",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + eContact));
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(call);
                }


            }
        });

    }

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallProgressing(Call calls) {
            Toast.makeText(getApplicationContext(),"Ring ring",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCallEstablished(Call calls) {
            Toast.makeText(getApplicationContext(),"Call connected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCallEnded(Call endedcall) {
            Toast.makeText(getApplicationContext(),"Call ended",Toast.LENGTH_SHORT).show();
            calls=null;
            endedcall.hangup();
        }

        @Override
        public void onShouldSendPushNotification(Call calls, List<PushPair> list) {

        }
    }




    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, final Call incomingcall) {

            AlertDialog alertDialog =new AlertDialog.Builder(MainMenu.this).create();
            alertDialog.setTitle("Calling...");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reject", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    calls=incomingcall;
                    dialog.dismiss();
                    calls.hangup();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Pick up", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    calls=incomingcall;
                    calls.answer();
                    calls.addCallListener(new MainMenu.SinchCallListener());
                    Toast.makeText(getApplicationContext(),"Call is answered",Toast.LENGTH_LONG).show();
                }
            });

            alertDialog.show();
        }
    }
}

