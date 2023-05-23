package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tictacjournalofficial.Delete_Restore;
import com.example.tictacjournalofficial.R;

public class LoginAndRestore extends AppCompatActivity {

    Button btnBackuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_restore);

        btnBackuo = findViewById(R.id.btnBackup);

        btnBackuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAndRestore.this, Delete_Restore.class);
                startActivity(intent);
            }
        });
    }


}