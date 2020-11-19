package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfilePage extends AppCompatActivity {
    TextView Name,DOB,Address,EmergencyContact,ConfirmButton;
    DatabaseReference databaseReference;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
        Name=findViewById(R.id.Name1);
        Address=findViewById(R.id.Address1);
        DOB =findViewById(R.id.dob1);
        EmergencyContact=findViewById(R.id.EnterEmergency1);
        ConfirmButton=findViewById(R.id.Confirm1);
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
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NewContact;
                NewContact = EmergencyContact.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Member").child(userid).child("emergencyContact").setValue(NewContact);
                Intent GoBackToProfile=new Intent(EditProfilePage.this,ProfilePage.class);
                GoBackToProfile.putExtra("uid", userid);
                startActivity(GoBackToProfile);
                finish();
            }
        });

    }
}