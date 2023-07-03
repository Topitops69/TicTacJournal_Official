package com.example.tictacjournalofficial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictacjournalofficial.Firebase.CreateAccount;
import com.example.tictacjournalofficial.Firebase.Login;
import com.example.tictacjournalofficial.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Enable offline data persistence
        db.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());

        button = findViewById(R.id.button);

        button.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        });

        TextView textView;

        textView = findViewById(R.id.textView);
        textView.setOnClickListener(view -> {
            Intent intent1 = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(intent1);
            finish();

            Toast.makeText(MainActivity.this, "Login and restore", Toast.LENGTH_LONG).show();
        });
    }
}
