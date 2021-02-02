package com.example.fariscare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class LanguageSettings extends AppCompatActivity {
    Button lang;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_settings);
        Spinner spinner = findViewById(R.id.spinner);
        lang = (Button)findViewById(R.id.langbutton);
        back = (ImageButton)findViewById(R.id.backarrow);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = spinner.getSelectedItem().toString();
                Toast.makeText(LanguageSettings.this,lang,Toast.LENGTH_SHORT).show();
                setLocale(lang);
                setAppLocale(lang);
                recreate();
            }
        });
    }

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
    }
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings",MODE_PRIVATE);
        String lang = prefs.getString("Lang","");
        setLocale(lang);
    }
}
