package com.example.fariscare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.EventLog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fariscare.Adapters.EventItem;
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
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final String TAG="Recycler View ItemList";
    private ArrayList<EventItem> mItemList; //Keith
    private RecyclerView mRecyclerView; //Contain recycler view recreated in XML layout
    private EventAdapter mAdapter; //Bridge between Arraylist and recyclerview
    private RecyclerView.LayoutManager mLayoutManager; //Responsible for aligning items in Arraylist
    private Button buttonInsert; //Keith
    private Button buttonRemove; //Keith
    private EditText editTextInsert;
    private EditText editTextRemove;
    public Context logincontext;
    Button loginbutton;
    TextView register;
    EditText EnterEmail,EnterPassword;
    Button Loginbutton, langButton;
    TextView RegisterButton;
    TextView ResetPassword;
    FirebaseAuth Auth;
    SharedPreferences Auto_login;
    FirebaseUser user;
    String uid;
    DatabaseReference databaseReference;
    ProgressBar progressBar;

    public MainActivity(Context context)
    {
        logincontext=context;
    }
    public Context getLogincontext(){
        return logincontext;
    }
    public  Context getContext(){
        Context mContext = MainActivity.this;
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auto_login=getSharedPreferences("LoginButton",MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        loginbutton=(Button)findViewById(R.id.LoginButton);
        register=(TextView)findViewById(R.id.Register);
        langButton = (Button)findViewById(R.id.langbutton);
        EnterEmail=findViewById(R.id.EnterEmail);
        EnterPassword=findViewById(R.id.EnterPassword);
        Auth=FirebaseAuth.getInstance();
        progressBar =  findViewById(R.id.progressBar);
        user=Auth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member");
        loadLocale();
    //chris
        //Auto_login.edit().putBoolean("logged",false).apply();
        //Chris - User is already logged in
        if(Auto_login.getBoolean("logged",false)){
            databaseReference.orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Chris - get uid from shared preferences
                        uid = Auto_login.getString("UserID", null);
                        Log.v(TAG,"the user id sent= " + uid);
                        Intent MainActivity = new Intent(MainActivity.this, MainMenu.class);
                        MainActivity.putExtra("User_UID", uid);
                        startActivity(MainActivity);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String email, password;
                email = EnterEmail.getText().toString().trim();
                password = EnterPassword.getText().toString().trim();
                //Chris - Validation
                //Check for empty Inputs
                if (email.isEmpty()) {
                    Log.v(TAG,"Email Required");
                    Toast.makeText(MainActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty())//check for empty input
                {
                    Log.v(TAG,"Password Required");
                    Toast.makeText(MainActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6)//for exception to not appear
                {
                    Log.v(TAG,"Password is must be at least contain 6 characters");
                    Toast.makeText(MainActivity.this, "Password is must be at least contain 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Chris - find user id for the login user
                            databaseReference.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        uid = snapshot.getKey();
                                        String emergencycontact=snapshot.child("emergencyContact").getValue().toString();
                                        String name = snapshot.child("name").getValue().toString();

                                        //Chris - show that it works
                                        Log.v(TAG, "the user id sent= " + uid);

                                        //Chris -  save user id in shared preference
                                        SharedPreferences.Editor editor = Auto_login.edit();
                                        editor.putString("UserID", uid).apply();
                                        //Chris - if login is successful
                                        //Chris - Intent to homepage and pass user id to it
                                        Intent ToMenuPage = new Intent(MainActivity.this, MainMenu.class);
                                        ToMenuPage.putExtra("User_UID", uid);
                                        ToMenuPage.putExtra("emergency", emergencycontact);
                                        Auto_login.edit().putBoolean("logged", true).apply();
                                        Log.v(TAG, "sending this uid to main activity " + uid);
                                        startActivity(ToMenuPage);
                                        finish();


                                    }

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });


                        }
                        else {
                            //Chris - if login failed
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.v(TAG,"Login Failed");
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent RegisterIntent=new Intent(MainActivity.this,RegisterPage.class);
                startActivity(RegisterIntent);
            }
        });

        //Faris
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this,R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        langButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = spinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this,lang,Toast.LENGTH_SHORT).show();
                setLocale(lang);
                setAppLocale(lang);
                recreate();
            }
        });


    }
    //Faris, changes locale and language
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("Lang", lang);
        editor.apply();


        //----------------------------------------------------------------------------------------------------------------------------------
        //Recyclerview for events-----------------------------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------------------------------------------
        createItemList();
        buildRecyclerView();

        buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });

    }

    public void insertItem(int position) {
        mItemList.add(position, new EventItem(R.drawable.ic_test_img, "New Item At Position" + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItemList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        mItemList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createItemList() {
        mItemList = new ArrayList<>();
        mItemList.add(new EventItem(R.drawable.ic_test_img, "Line 1", "Line 2"));
        mItemList.add(new EventItem(R.drawable.ic_test_img2, "Line 3", "Line 4"));
        mItemList.add(new EventItem(R.drawable.ic_test_img3, "Line 5", "Line 6"));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EventAdapter(mItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }
        });
    }
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings",MODE_PRIVATE);
        String lang = prefs.getString("Lang","");
        setLocale(lang);
    }


}