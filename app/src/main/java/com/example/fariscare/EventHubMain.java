package com.example.fariscare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventHubMain extends AppCompatActivity {
    Button Keith,Azzi,Chris,Home;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_hub_main);
        Azzi=findViewById(R.id.AddEvents);
        Keith=findViewById(R.id.ViewPublicEvents);
        Chris=findViewById(R.id.SearchPublicEvents);
        Home= findViewById(R.id.BackToHome);
        Bundle bundle = getIntent().getExtras();
        uid=bundle.getString("User_UID");
        Azzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Add=new Intent(EventHubMain.this,ViewPersonalEvent.class);
                Add.putExtra("User_UID", uid);
                startActivity(Add);
                finish();
            }
        });
        Keith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent View=new Intent(EventHubMain.this,ViewPublicEvents.class);
                View.putExtra("User_UID", uid);
                startActivity(View);
                finish();
            }
        });
        Chris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Search=new Intent(EventHubMain.this,Search.class);
                Search.putExtra("User_UID", uid);
                startActivity(Search);
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Search=new Intent(EventHubMain.this,MainMenu.class);
                Search.putExtra("User_UID", uid);
                startActivity(Search);
                finish();
            }
        });
    }
}