package com.example.tictacjournalofficial.Firebase;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.tictacjournalofficial.entities.Journal;

import java.util.List;

public class JournalViewModel extends ViewModel {
    private JournalRepository repository;

    public JournalViewModel(Context context) {
        repository = new JournalRepository(context);
    }

    public List<Journal> getAllJournals() {
        return repository.getAllJournals();
    }

    public void insertJournal(Journal journal) {
        repository.insertJournal(journal);
    }

    public void deleteJournal(Journal journal) {
        repository.deleteJournal(journal);
    }
}
