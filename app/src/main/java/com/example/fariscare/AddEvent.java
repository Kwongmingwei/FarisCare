package com.example.fariscare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddEvent extends AppCompatActivity {
    private static final String TAG="Add Event";
    TextView Title,Date,Time,Notes,String1,String2,String3,String4;
    Button button;
    String uid;
    PublicEventSearch publicEventSearch;
    long maxid=0;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Bundle bundle = getIntent().getExtras();
        uid=bundle.getString("User_UID");
        Title=findViewById(R.id.Private);
        String1=findViewById(R.id.title1);

        Date=findViewById(R.id.Date);
        String2=findViewById(R.id.date);

        Time=findViewById(R.id.Time);
        String3=findViewById(R.id.title1);

        Notes=findViewById(R.id.Notes);
        String4=findViewById(R.id.notes);
        button=findViewById(R.id.add1);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Public Events");
        publicEventSearch= new PublicEventSearch();
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title, date, time, note;
                title = String1.getText().toString();
                date = String2.getText().toString();
                time = String3.getText().toString();
                note = String4.getText().toString();
                //Chris - Verification for inputs
                //Chris - Check for empty Inputs
                if (title.equals("")) {
                    Log.v(TAG, "Title Required");
                    Toast.makeText(AddEvent.this, "Title Required", Toast.LENGTH_SHORT).show();
                    return;

                }
                publicEventSearch.setEventID(String.valueOf(maxid + 1));
                publicEventSearch.seteventName(title);
                publicEventSearch.setEventDate(date);
                publicEventSearch.setEventTime(time);
                publicEventSearch.setEventDesc(note);
                publicEventSearch.setParticipants(uid);
                publicEventSearch.seteventType("Private");
                String idvalue = String.valueOf(maxid + 1);
                databaseReference.child(idvalue).setValue(publicEventSearch);
                Intent Main=new Intent(AddEvent.this,MainMenu.class);
                Main.putExtra("User_UID", uid);
                startActivity(Main);
                finish();
            }
        });
    }
}