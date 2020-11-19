package com.example.fariscare;

import android.Manifest;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class MainMenu extends AppCompatActivity {
    Button emergencyButton;
    Button socialButton;
    String eContact,uid;
    TextView address,name,dob;
    DatabaseReference databaseReference;
    ImageView ic,call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ActivityCompat.requestPermissions(MainMenu.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        socialButton=(Button)findViewById(R.id.social);
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
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent social = new Intent(MainMenu.this, OCall.class);
                startActivity(social);
            }
            });
        socialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAudioPermissions();
                Intent social=new Intent(MainMenu.this,OCall.class);
                startActivity(social);

            }
        });
        ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile=new Intent(MainMenu.this,ProfilePage.class);
                profile.putExtra("uid", uid);
                startActivity(profile);
                finish();
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
}

