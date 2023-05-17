package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tictacjournalofficial.R;

public class Theme extends AppCompatActivity {


    Button btnContinue, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        //getSupportActionBar().setTitle("Theme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);

        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(View-> startActivity(new Intent(Theme.this, Welcome_end.class)));

        btnContinue.setOnClickListener(view-> startActivity(new Intent(Theme.this, Welcome_end.class)));
    }
}