package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.quotes.QuotesList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addJournal extends AppCompatActivity {
    private EditText inputJournalTitle, inputJournalSubtitle, inputJournalText;
    private TextView textDateTime;
    private final List<QuotesList> quotesListList = new ArrayList<>();
    //color
    private String selectedJournalColor;
    private View viewSubtitleIndicator;
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
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v ->  saveJournal());


        //default color
        selectedJournalColor ="#333333";
        //initialize the color here:
        initMiscellaneous();
        setSubtitleIndicatorColor();
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
        journal.setColor(selectedJournalColor);
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

    //Color part code
    private void initMiscellaneous(){
        final LinearLayout  layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#fdbe3b";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#ff4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicatorColor();
            }
        });
    }

    private void setSubtitleIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedJournalColor));
    }
}