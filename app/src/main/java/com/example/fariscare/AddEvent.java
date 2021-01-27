package com.example.fariscare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddEvent extends AppCompatActivity {
    private static final String TAG="Add Event";
    TextView Title,Date,Time,Notes,String1,String2,String3,String4;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Title=findViewById(R.id.Private);
        String1=findViewById(R.id.title1);

        Date=findViewById(R.id.Date);
        String2=findViewById(R.id.date);

        Time=findViewById(R.id.Time);
        String3=findViewById(R.id.title1);

        Notes=findViewById(R.id.Notes);
        String4=findViewById(R.id.notes);
        button=findViewById(R.id.add1);

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
            }
        });
    }
}