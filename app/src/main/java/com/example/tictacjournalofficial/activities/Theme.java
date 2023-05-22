package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tictacjournalofficial.R;

public class Theme extends AppCompatActivity {

    ImageButton btnLight, btnDark;

    Button btnContinue, btnSkip;
    boolean nightMODE;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        getSupportActionBar().hide();

        btnLight = findViewById(R.id.btnLight);
        btnDark = findViewById(R.id.btnDark);
        btnSkip = findViewById(R.id.btnSkip);
        btnContinue = findViewById(R.id.btnContinue);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);

        btnLight.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", false);
            editor.apply();
            updateTheme();
        });

        btnDark.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("night", true);
            editor.apply();
            updateTheme();
        });

        btnSkip.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(Theme.this, Welcome_end.class)));
        });
        btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(new Intent(Theme.this, Welcome_end.class)));
        });
    }

    private void updateTheme() {
        if(nightMODE) {
            btnLight.setVisibility(View.VISIBLE);
            btnDark.setVisibility(View.GONE);
        } else {
            btnLight.setVisibility(View.GONE);
            btnDark.setVisibility(View.VISIBLE);
        }
    }

}
