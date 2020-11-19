package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
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
    TextView Name,DOB,Address,EmergencyContact;
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
        Bundle bundle = getIntent().getExtras();
        userid=bundle.getString("uid");
        Toast.makeText(ProfilePage.this,userid,Toast.LENGTH_SHORT).show();
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
    }
}