package com.example.tictacjournalofficial.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.databinding.ActivityHomeBinding;
import com.example.tictacjournalofficial.fragments.InsightFragment;
import com.example.tictacjournalofficial.fragments.Journal1Fragment;
import com.example.tictacjournalofficial.fragments.SettingsFragment;
import com.example.tictacjournalofficial.fragments.XOXOFragment;




public class Home extends AppCompatActivity {

    public static int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    ActivityHomeBinding binding;
    //fragments
    private Journal1Fragment currentFragment;


    //practise
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

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


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home, fragment);
        fragmentTransaction.commit();

        if (fragment instanceof Journal1Fragment) {
            currentFragment = (Journal1Fragment) fragment;
        } else {
            currentFragment = null;
        }
    }


    @Override
    public void onBackPressed() {
        if (currentFragment != null && currentFragment.getInputSearch().hasFocus()) {
            currentFragment.getInputSearch().clearFocus();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Go back?")
                    .setMessage("Are you sure you want to go back? Any unsaved changes will be lost.")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> Home.super.onBackPressed()).create().show();
        }
    }

}