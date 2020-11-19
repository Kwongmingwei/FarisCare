package com.example.fariscare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterPt2 extends AppCompatActivity {
    EditText PhoneNo,Address,PostalCode,DOB;
    Button RegisterButton;
    FirebaseAuth Auth;
    DatabaseReference databaseReference;
    Member member;
    ProgressBar progressBar;
    long maxid=0;
    String email,password,name;
    private static final String TAG = "RegisterPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pt2);
        PhoneNo=findViewById(R.id.EnterPhoneNo);
        Address=findViewById(R.id.EnterAddress);
        PostalCode =findViewById(R.id.EnterPostal);
        DOB =findViewById(R.id.EnterDOB);
        RegisterButton=findViewById(R.id.RegisterButton);
        progressBar =  findViewById(R.id.progressBar);
        Auth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member");
        member = new Member();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    //Chris - To get the current number of users in the database.
                    maxid=dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                final String phoneNo,address,postal,dob;
                address = Address.getText().toString();
                phoneNo = PhoneNo.getText().toString();
                postal = PostalCode.getText().toString();
                dob=DOB.getText().toString();
                //Chris - Verification for inputs
                //Chris - Check for empty Inputs

                if (phoneNo.equals("")) {
                    Log.v(TAG, "Phone Number Required");//Chris - Check for empty Inputs
                    Toast.makeText(RegisterPt2.this, "Phone Number Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dob.equals("")) {
                    Log.v(TAG, "Date of Birth Required");
                    Toast.makeText(RegisterPt2.this, "Date of Birth Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (address.equals("")) {
                    Log.v(TAG, "Address Required");
                    Toast.makeText(RegisterPt2.this, "Address Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (postal.equals(""))//Chris - Check for empty Inputs
                {
                    Log.v(TAG, "Postal Code Required");
                    Toast.makeText(RegisterPt2.this, "Postal Code Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);//Chris - For user to know that the data is being processed
                    Bundle bundle = getIntent().getExtras();
                    name=bundle.getString("name");
                    email=bundle.getString("email");
                    password=bundle.getString("password");
                    //Authentication
                    Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = Auth.getCurrentUser();
                            String id = user.getUid();
                            member.setUserID(id);
                            Log.v("Reg2", "UID is" + id);
                            progressBar.setVisibility(View.INVISIBLE);
                            //Chris - Register is successful,saving user details to firebase database
                            member.setName(name);
                            member.setEmail(email);
                            member.setPassword(password);
                            member.setPhoneNo(phoneNo);
                            member.setAddress(address);
                            //87115223
                            member.setEmergencyContact("");
                            member.setRequestedItems("");
                            member.setRequestHistory("");
                            member.setProfilePic("");
                            member.setPostalCode(postal);
                            member.setAccountType("Elderly");
                            member.setDOB(dob);
                            //Chris -  Customised user id,make sure no two users have the same user id
                            String idvalue = String.valueOf(maxid + 1);
                            //Chris - Add the user to firebase database
                            databaseReference.child(idvalue).setValue(member);
                            Log.v(TAG, "Registered Successfully");
                            Toast.makeText(RegisterPt2.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(RegisterPt2.this, MainActivity.class);
                            startActivity(login);
                            finish();
                        }
                    });
                }
            }
        });

    }

}
