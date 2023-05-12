package com.example.tictacjournalofficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    Button btnContinue, btnSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //getSupportActionBar().setTitle("Welcome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);

        btnSkip.setOnClickListener(View-> startActivity(new Intent(Welcome.this, Password.class)));

        btnContinue.setOnClickListener(view-> startActivity(new Intent(Welcome.this, Password.class)));

    }
}