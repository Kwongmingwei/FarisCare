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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    EditText EnterEmail,EnterPassword,EnterName,ConfirmPassword,PhoneNo;
    Button RegisterButton;
    FirebaseAuth Auth;
    DatabaseReference databaseReference;
    Member member;
    String number;
    ProgressBar progressBar;
    long maxid=0;
    private static final String TAG = "RegisterPage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        EnterEmail=findViewById(R.id.EnterEmail);
        EnterName=findViewById(R.id.EnterName);
        EnterPassword=findViewById(R.id.EnterPassword);
        ConfirmPassword=findViewById(R.id.ConfirmPassword);
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
                String email, password, name, confirmPassword,KEY;
                email = EnterEmail.getText().toString();
                password = EnterPassword.getText().toString();
                name = EnterName.getText().toString();
                confirmPassword = ConfirmPassword.getText().toString();
                //Chris - Verification for inputs
                //Chris - Check for empty Inputs
                if (name.equals("")) {
                    Log.v(TAG, "Name Required");
                    Toast.makeText(RegisterPage.this, "Name Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.equals("")) {
                    Log.v(TAG, "Email Required");//Chris - Check for empty Inputs
                    Toast.makeText(RegisterPage.this, "Email Required", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (password.equals(""))//Chris - Check for empty Inputs
                {
                    Log.v(TAG, "Password Required");
                    Toast.makeText(RegisterPage.this, "Password Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6)//Chris - to check password hit the minimal characters of the password requirement
                {
                    Log.v(TAG, "Password is must be at least contain 6 characters");
                    Toast.makeText(RegisterPage.this, "Password is must be at least contain 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.equals(""))//Chris - Check for empty Inputs
                {
                    Log.v(TAG, "Confirm Password");
                    Toast.makeText(RegisterPage.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!confirmPassword.equals(password))//Chris - To Confirm password
                {
                    Log.v(TAG, "Password Do Not Match");
                    Toast.makeText(RegisterPage.this, "Password Do Not Match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.equals(""))//Chris - Check for empty Inputs
                {
                    Log.v(TAG, "Confirm Password");
                    Toast.makeText(RegisterPage.this, "Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    progressBar.setVisibility(View.VISIBLE);//Chris - For user to know that the data is being processed
                    //Authentication
                    Auth.signInWithEmailAndPassword(email, confirmPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Chris - check whether register of user is successful or not
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                //Custom message if email is invaild
                                Toast.makeText(RegisterPage.this, "The email is invaild", Toast.LENGTH_SHORT).show();
                                Log.v(TAG, "The email is invaild");
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent login = new Intent(RegisterPage.this, RegisterPt2.class);
                                login.putExtra("email",email);
                                login.putExtra("password",confirmPassword);
                                startActivity(login);
                            }
                        }
                    });
                }
            }
        });

    }

}
