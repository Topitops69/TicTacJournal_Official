package com.example.tictacjournalofficial.Firebase;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictacjournalofficial.R;

public class Delete_Restore extends AppCompatActivity {

//    FirebaseFirestore firestore;
    Button btnSync;
    Button btnRestore;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_restore);

//        firestore = FirebaseFirestore.getInstance();
        btnSync = findViewById(R.id.btnSync);
        btnRestore = findViewById(R.id.btnRestore);

        btnSync.setOnClickListener(v -> {
          //  syncData();
        });

        btnRestore.setOnClickListener(v -> {
           // restoreData();
        });

        btnDelete.setOnClickListener(v -> {
            //deleteData();
        });
    }

}
