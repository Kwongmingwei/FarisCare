package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPt2 extends AppCompatActivity {
    EditText PhoneNo,Address,PostalCode;
    Button RegisterButton;
    FirebaseAuth Auth;
    DatabaseReference databaseReference;
    Member member;
    ProgressBar progressBar;
    long maxid=0;
    String email,password;
    private static final String TAG = "RegisterPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pt2);
        PhoneNo=findViewById(R.id.EnterPhoneNo);
        Address=findViewById(R.id.EnterAddress);
        PostalCode =findViewById(R.id.EnterPostal);
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
                final String phoneNo,address,postal;
                address = Address.getText().toString();
                phoneNo = PhoneNo.getText().toString();
                postal = PostalCode.getText().toString();
                //Chris - Verification for inputs
                //Chris - Check for empty Inputs

                if (phoneNo.equals("")) {
                    Log.v(TAG, "Phone number Required");//Chris - Check for empty Inputs
                    Toast.makeText(RegisterPt2.this, "Phone number Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (address.equals("")) {
                    Log.v(TAG, "Address Required");
                    Toast.makeText(RegisterPt2.this, "Address Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (postal.equals(""))//Chris - Check for empty Inputs
                {
                    Log.v(TAG, "Postal code Required");
                    Toast.makeText(RegisterPt2.this, "Password Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);//Chris - For user to know that the data is being processed
                    Bundle bundle = getIntent().getExtras();
                    email=bundle.getString("email");
                    password=bundle.getString("password");
                    Toast.makeText(RegisterPt2.this, password, Toast.LENGTH_SHORT).show();

                    //Authentication
                    Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Chris - check whether register of user is successful or not
                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                //Custom message if email is invaild
                                Log.v(TAG, "The email is invaild");
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                //Chris - Register is successful,saving user details to firebase database
                                long id=maxid + 1;
                                member.setUserID(id);
                                member.setName("name");
                                member.setEmail(email);
                                member.setPassword(password);
                                member.setPhoneNo(phoneNo);
                                member.setAddress(address);
                                member.setEmergencyContact("");
                                member.setRequestedItems("");
                                member.setRequestHistory("");
                                member.setProfilePic("");
                                member.setPostalCode(postal);
                                member.setAccountType("Elderly");

                                //Chris -  Customised user id,make sure no two users have the same user id
                                String idvalue = String.valueOf(maxid + 1);
                                //Chris - Add the user to firebase database
                                databaseReference.child(idvalue).setValue(member);
                                Log.v(TAG, "Registered Successfully");
                                Toast.makeText(RegisterPt2.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(RegisterPt2.this, MainActivity.class);
                                startActivity(login);
                            }
                        }
                    });
                }
            }
        });

    }

}
