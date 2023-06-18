package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.Firebase.CreateAccount;

public class Welcome extends AppCompatActivity {

    Button btnContinue, btnSkip;
    TextView tfRestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //getSupportActionBar().setTitle("Welcome");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);
        tfRestore = findViewById(R.id.tfRestore);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, Password.class));
                finish();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, Password.class));
                finish();
            }
        });

        tfRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome.this, CreateAccount.class));
            }
        });

    }


}
