package com.example.tictacjournalofficial.Firebase;

import android.os.Bundle;
import android.view.View;
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

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  syncData();
            }
        });

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // restoreData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteData();
            }
        });
    }
//
//    private void syncData() {
//        List<Journal> yourJournalsList = JournalsDatabase.getDatabase(this).journalDao().getAllJournals();
//
//        for (Journal journal : yourJournalsList) {
//            firestore.collection("journals")
//                    .document(String.valueOf(journal.getId())) // using journal's ID as document ID
//                    .set(journal)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            // successfully uploaded
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // failed to upload
//                        }
//                    });
//        }
//    }
//
//    private void restoreData() {
//        firestore.collection("journals")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<Journal> downloadedJournals = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Journal journal = document.toObject(Journal.class);
//                                downloadedJournals.add(journal);
//                            }
//
//                            // At this point, downloadedJournals contains all your Journals from Firestore.
//                            // You can now update your local data with this.
//                            JournalsDatabase.getDatabase(Delete_Restore.this).journalDao().deleteAllJournals(); // delete all existing journals
//                            JournalsDatabase.getDatabase(Delete_Restore.this).journalDao().insertAll(downloadedJournals); // insert downloaded journals
//                        } else {
//                            // Log error
//                        }
//                    }
//                });
//    }
//
//    private void deleteData() {
//        new AlertDialog.Builder(this)
//                .setTitle("Delete all journals")
//                .setMessage("Are you sure you want to delete all your journals?")
//                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // User wants to delete all data
//                        JournalsDatabase.getDatabase(Delete_Restore.this).journalDao().deleteAllJournals(); // delete all journals
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }

}
