package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.tictacjournalofficial.R;

public class Welcome_end extends AppCompatActivity {
    Button btnGot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_end);
        //getSupportActionBar().setTitle("End");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnGot = findViewById(R.id.btnGot);

        btnGot.setOnClickListener(view -> {
            startActivity(new Intent(Welcome_end.this, Home.class));
            finish();
        });
    }
}
