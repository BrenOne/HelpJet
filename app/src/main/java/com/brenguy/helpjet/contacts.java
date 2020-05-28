package com.brenguy.helpjet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class contacts extends AppCompatActivity {

    public static SharedPreferences sPref;
    public String SAVED_PHONE1 = "";
    public String SAVED_PHONE2 = "";
    public String SAVED_PHONE3 = "";
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button save = findViewById(R.id.save);
        loadText();
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveText();
            }
        });
        Button back = findViewById(R.id.goback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(contacts.this, MainActivity.class));
            }
        });
    }
    public void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        EditText etText1 = findViewById(R.id.ph1);
        EditText etText2 = findViewById(R.id.ph2);
        EditText etText3 = findViewById(R.id.ph3);
        ed.putString("SAVED_PHONE1", etText1.getText().toString());
        ed.putString("SAVED_PHONE2", etText2.getText().toString());
        ed.putString("SAVED_PHONE3", etText3.getText().toString());
        ed.commit();
        Toast.makeText(this, "Phones saved", Toast.LENGTH_SHORT).show();
    }
    public void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        EditText etText1 = findViewById(R.id.ph1);
        EditText etText2 = findViewById(R.id.ph2);
        EditText etText3 = findViewById(R.id.ph3);
        String savedText1 = sPref.getString("SAVED_PHONE1", "");
        String savedText2 = sPref.getString("SAVED_PHONE2", "");
        String savedText3 = sPref.getString("SAVED_PHONE3", "");
        etText1.setText(savedText1);
        etText2.setText(savedText2);
        etText3.setText(savedText3);
        memory.SAVED_PHONE1 = sPref.getString("SAVED_PHONE1", "");
        memory.SAVED_PHONE2 = sPref.getString("SAVED_PHONE2", "");
        memory.SAVED_PHONE3 = sPref.getString("SAVED_PHONE3", "");
    }
}

