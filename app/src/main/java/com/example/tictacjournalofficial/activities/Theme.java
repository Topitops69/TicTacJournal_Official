package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tictacjournalofficial.R;

public class Theme extends AppCompatActivity {

    Button btnContinue, btnSkip;
    ImageButton btnLight, btnDark;
    TextView lblMode;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.layout); // Make sure to set the id of ConstraintLayout in your XML to 'layout'
        btnLight = findViewById(R.id.btnLight);
        btnDark = findViewById(R.id.btnDark);
        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);

        lblMode = findViewById(R.id.lblMode);
        btnLight.setOnClickListener(v -> {
            lightMode();
        });

        btnSkip.setOnClickListener(View-> startActivity(new Intent(Theme.this, Welcome_end.class)));
        btnContinue.setOnClickListener(view-> startActivity(new Intent(Theme.this, Welcome_end.class)));
    }

    void lightMode() {
        layout.setBackgroundColor(Color.WHITE);
        lblMode.setTextColor(Color.BLACK);
    }

    void darkMode() {

    }
}
