package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.Firebase.CreateAccount;
import com.example.tictacjournalofficial.Firebase.Login;
import com.example.tictacjournalofficial.R;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });

        TextView textView;

        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(intent1);
            finish();

            Toast.makeText(MainActivity.this, "Login and restore", Toast.LENGTH_LONG).show();
        });
    }


}
