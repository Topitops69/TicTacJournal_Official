package com.example.tictacjournalofficial.Firebase;

import android.content.Context;

import com.example.tictacjournalofficial.dao.JournalsDao;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class JournalRepository {
    private final JournalsDao journalsDao;
    private final FirebaseFirestore firestoreDb;

    public JournalRepository(Context context) {
        JournalsDatabase db = JournalsDatabase.getDatabase(context);
        journalsDao = db.journalDao();
        firestoreDb = FirebaseFirestore.getInstance();
    }

    // Fetch all journals from local database
    public List<Journal> getAllJournals() {
        return journalsDao.getAllJournals();
    }

    // Insert a journal into local database
    public void insertJournal(Journal journal) {
        new Thread(() -> journalsDao.insertJournal(journal)).start();
    }

    // Delete a journal from local database
    public void deleteJournal(Journal journal) {
        new Thread(() -> journalsDao.deleteJournal(journal)).start();
    }

    // Insert a journal into Firestore
    public Task<DocumentReference> insertJournalToFirestore(Journal journal) {
        return firestoreDb.collection("journals").add(journal);
    }

    // Fetch all journals from Firestore
    // It's a more complicated operation and should handle synchronization, which is a larger topic
    // For now, just understand this operation would be here.
}

