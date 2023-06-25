package com.example.tictacjournalofficial.adapters;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.bumptech.glide.Glide;
import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.addJournal;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.listeners.JournalsListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class JournalsAdapter extends FirestoreRecyclerAdapter<Journal, JournalsAdapter.JournalViewHolder> {

    private JournalsListeners journalsListeners;
    private Timer timer;
    private static List<Journal> journalsSource;

    private String applicationID = "2HDEDMVLHZ";
    private String apiKey = "5d443008cfff038c4c9138511dc11a53";
    FirebaseFirestore db;
    Index index;
    Client client;
    private List<Journal> journalList = new ArrayList<>();

    public static List<Journal> getList(){
        return journalsSource;
    }

    public JournalsAdapter(@NonNull FirestoreRecyclerOptions<Journal> options, JournalsListeners journalsListeners) {
        super(options);
        this.journalsListeners = journalsListeners;
        this.journalsSource = new ArrayList<>();  // initialize the list here
        db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("journals");
        client = new Client(applicationID, "61e582e8f6bbd9f8a75679a9a9d9b30a");
        index = client.getIndex("journals");
    }


    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JournalViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_journal,
                        parent,
                        false

                )
        );

    }



    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, final int position, @NonNull Journal model) {
        // Retrieve docId from Firestore and set it in Journal model
        String docId = this.getSnapshots().getSnapshot(position).getId();
        model.setFirestoreId( docId); // you'll need to add a setDocId method in your Journal class

        holder.setJournal(model);
        holder.layoutJournal.setOnClickListener(v ->
                journalsListeners.onJournalClicked(model, position)
        );

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), addJournal.class);
            intent.putExtra("isViewOrUpdate", true);
            intent.putExtra("journal", model);
            intent.putExtra("docId", docId); // this docId will now always be available with your Journal model
            v.getContext().startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    static class JournalViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutJournal;
        RoundedImageView imageJournal;

        JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutJournal = itemView.findViewById(R.id.layoutNote);
            imageJournal = itemView.findViewById(R.id.imageJournal);
        }

        void setJournal(Journal journal){
            textTitle.setText(journal.getTitle());
            if(journal.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(journal.getSubtitle());
            }
            textDateTime.setText(journal.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutJournal.getBackground();
            if(journal.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(journal.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(journal.getImagePath() != null){
                Glide.with(itemView.getContext())
                        .load(journal.getImagePath())
                        .into(imageJournal);
                imageJournal.setVisibility(View.VISIBLE);
            } else {
                imageJournal.setVisibility(View.GONE);
            }
        }

    }

    public void searchJournals(String keyword) {
        Query query = new Query(keyword)
                .setAttributesToRetrieve("firestoreId")  // Make sure you're indexing the Firestore document ID in Algolia
                .setHitsPerPage(50);

        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                if (error != null) {
                    Log.e("AlgoliaError", "Algolia Search Error: " + error.getMessage());
                    return;
                }

                try {
                    JSONArray hits = content.getJSONArray("hits");
                    journalList.clear();

                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject jsonObject = hits.getJSONObject(i);

                        String firestoreId = jsonObject.getString("firestoreId");  // Get the Firestore document ID from the search result

                        // Now you can fetch the full data for this item from Firestore using the Firestore document ID
                        FirebaseFirestore.getInstance().collection("journals").document(firestoreId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Journal journal = documentSnapshot.toObject(Journal.class);
                                        journalList.add(journal);

                                        // Update journalsSource and refresh the RecyclerView
                                        journalsSource = journalList;
                                        notifyDataSetChanged();
                                    }
                                });
                    }

                } catch (JSONException e) {
                    Log.e("AlgoliaError", "Error parsing Algolia search results: " + e.getMessage());
                }
            }
        });
    }



    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }

}
