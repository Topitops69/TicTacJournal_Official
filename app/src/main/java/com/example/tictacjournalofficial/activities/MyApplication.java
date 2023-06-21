package com.example.tictacjournalofficial.activities;

import android.app.Application;

import com.example.tictacjournalofficial.entities.Journal;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

    }


}
