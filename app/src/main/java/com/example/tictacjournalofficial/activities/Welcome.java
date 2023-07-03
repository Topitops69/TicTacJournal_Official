package com.example.tictacjournalofficial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictacjournalofficial.Firebase.CreateAccount;
import com.example.tictacjournalofficial.R;

import java.util.Objects;

public class Welcome extends AppCompatActivity {

    Button btnContinue, btnSkip;
    TextView tfRestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //getSupportActionBar().setTitle("Welcome");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);
        tfRestore = findViewById(R.id.tfRestore);

        btnSkip.setOnClickListener(view -> {
            startActivity(new Intent(Welcome.this, Password.class));
            finish();
        });

        btnContinue.setOnClickListener(view -> {
            startActivity(new Intent(Welcome.this, Password.class));
            finish();
        });

        tfRestore.setOnClickListener(v -> startActivity(new Intent(Welcome.this, CreateAccount.class)));

    }


}
