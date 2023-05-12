package com.example.tictacjournalofficial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Welcome_end extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_end);
        //getSupportActionBar().setTitle("End");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}