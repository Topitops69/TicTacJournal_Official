package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.databinding.ActivityHomeBinding;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.fragments.InsightFragment;
import com.example.tictacjournalofficial.fragments.Journal1Fragment;
import com.example.tictacjournalofficial.fragments.SettingsFragment;
import com.example.tictacjournalofficial.fragments.XOXOFragment;

import java.util.List;


public class Home extends AppCompatActivity {
    //BottomNavigationView bottomNavigationView;

    public static int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
       // bottomNavigationView = findViewById(R.id.bottomNavigationView);

       // binding.bottomNavigationView.setBackground(null);
        replaceFragment(new Journal1Fragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.journal:
                    replaceFragment(new Journal1Fragment());
                    break;
                case R.id.XOXO:
                    replaceFragment(new XOXOFragment());
                    break;
                case R.id.insight:
                    replaceFragment(new InsightFragment());
                    break;
                case R.id.Settings:
                    replaceFragment(new SettingsFragment());
                    break;

            }
            return true;
        });

        ImageView imageAddNoteHome = findViewById(R.id.imageAddNoteMain);

        imageAddNoteHome.setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), addJournal.class),
                REQUEST_CODE_ADD_JOURNAL_NOTE

        ));

        getJournals();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.commit();
    }

    private void getJournals(){

        @SuppressLint("StaticFieldLeak")
        class GetJournalsTask extends AsyncTask<Void, Void, List<Journal>>{

            @Override
            protected List<Journal> doInBackground(Void... voids) {
                return JournalsDatabase.getDatabase(getApplicationContext()).journalDao().getAllJournals();
            }

            @Override
            protected void onPostExecute(List<Journal> journals) {
                super.onPostExecute(journals);
                Log.d("MY_JOURNALS", journals.toString());
            }
        }
        new GetJournalsTask().execute();

    }
}