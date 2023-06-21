package com.example.tictacjournalofficial.adapters;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.addJournal;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.listeners.JournalsListeners;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JournalsAdapter extends FirestoreRecyclerAdapter<Journal, JournalsAdapter.JournalViewHolder> {

    private JournalsListeners journalsListeners;
    private Timer timer;
    private List<Journal> journalsSource;

    public void filterList(List<Journal> newList){
        journalsSource = newList;
        notifyDataSetChanged();

    }

    public List<Journal> getList(){
        return journalsSource;
    }

    public JournalsAdapter(@NonNull FirestoreRecyclerOptions<Journal> options, JournalsListeners journalsListeners) {
        super(options);
        this.journalsListeners = journalsListeners;
        this.journalsSource = new ArrayList<>();  // initialize the list here
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

    public void searchJournals(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(searchKeyword.trim().isEmpty()){
                } else {
                    ArrayList<Journal> temp = new ArrayList<>();
                    for(Journal journal : journalsSource){
                        if(journal.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || journal.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || journal.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(journal);
                        }
                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }
}
