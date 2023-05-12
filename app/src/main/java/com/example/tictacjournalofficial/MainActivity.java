package com.example.tictacjournalofficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        button.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Welcome.class)));

        TextView textView;

        textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, LoginAndRestore.class);
            startActivity(intent1);

            Toast.makeText(MainActivity.this, "Login and restore", Toast.LENGTH_LONG).show();
        });

//
    }



}