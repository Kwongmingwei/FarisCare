package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProfilePage extends AppCompatActivity {
    TextView Name,DOB,Address,EmergencyContact,Edit;
    Button RegisterButton;
    FirebaseAuth Auth;
    DatabaseReference databaseReference;
    Member member;
    String userid;
    private static final String TAG = "ProfilePage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Name=findViewById(R.id.Name);
        Address=findViewById(R.id.Address);
        DOB =findViewById(R.id.dob);
        EmergencyContact=findViewById(R.id.emergency);
        Edit=findViewById(R.id.edit);
        Bundle bundle = getIntent().getExtras();
        userid=bundle.getString("uid");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n = dataSnapshot.child("name").getValue().toString();
                String birth = dataSnapshot.child("dob").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String emergency = dataSnapshot.child("emergencyContact").getValue().toString();
                Name.setText(n);
                Address.setText(address);
                DOB.setText(birth);
                EmergencyContact.setText(emergency);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditProfile=new Intent(ProfilePage.this,EditProfilePage.class);
                EditProfile.putExtra("uid", userid);
                startActivity(EditProfile);
                finish();
            }
        });
    }
}