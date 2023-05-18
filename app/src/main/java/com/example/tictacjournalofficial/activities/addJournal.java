package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.quotes.QuotesList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addJournal extends AppCompatActivity {
    private EditText inputJournalTitle, inputJournalSubtitle, inputJournalText;
    private TextView textDateTime;
    private final List<QuotesList> quotesListList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputJournalTitle = findViewById(R.id.inputJournal); //title ni siya
        inputJournalSubtitle = findViewById(R.id.inputSubtitle); //iyang subtitle
        inputJournalText = findViewById(R.id.inputJournalNote);//iyang note

        textDateTime = findViewById(R.id.textDateTime);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v ->  saveJournal());
    }



    private void saveJournal(){
        if(inputJournalTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Hey care to add a title?", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (inputJournalSubtitle.getText().toString().trim().isEmpty()
                && inputJournalText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Hey, your secret is safe with me. Let's try that again ok?", Toast.LENGTH_SHORT).show();
            return;
        }

        final Journal journal = new Journal();
        journal.setTitle(inputJournalTitle.getText().toString());
        journal.setSubtitle(inputJournalSubtitle.getText().toString());
        journal.setNoteText(inputJournalText.getText().toString());
        journal.setDateTime(textDateTime.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class SaveJournalTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                JournalsDatabase.getDatabase(getApplicationContext()).journalDao().insertJournal(journal);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        }

        new SaveJournalTask().execute();

    }
}