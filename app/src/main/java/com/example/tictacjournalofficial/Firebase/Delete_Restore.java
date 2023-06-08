//package com.example.tictacjournalofficial.Firebase;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.tictacjournalofficial.R;
//import com.example.tictacjournalofficial.entities.Journal;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Delete_Restore extends AppCompatActivity {
//
//    FirebaseFirestore firestore;
//    Button btnSync;
//    Button btnRestore;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_delete_restore);
//
//        firestore = FirebaseFirestore.getInstance();
//        btnSync = findViewById(R.id.btnSync);
//
//        btnSync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                syncData();
//            }
//        });
//    }
//
//    private void syncData() {
//        List<Journal> yourJournalsList = ... // get your data
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
//
//    //Restore function:
//   btnRestore = findViewById(R.id.btnRestore);
//    // inside onCreate
//    btnRestore.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            restoreData();
//        }
//    });
//
//    // new method
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
//                        } else {
//                            // Log error
//                        }
//                    }
//                });
//    }
//
//}
//
