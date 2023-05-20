package com.example.tictacjournalofficial.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.listeners.JournalsListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import android.os.Handler;
import android.os.Looper;


public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.JournalViewHolder>{

    private List<Journal> journals;
    private JournalsListeners journalsListeners;
    private Timer timer;
    private List<Journal> journalsSource;

    public JournalsAdapter(List<Journal> journals, JournalsListeners journalsListeners){
        this.journals = journals;
        this.journalsListeners = journalsListeners;
        journalsSource = journals;
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

//    @Override
//    public void onBindViewHolder(@NonNull JournalViewHolder holder, final int position) {
//            holder.setNote(journals.get(position));
//            holder.layoutJournal.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    journalsListeners.onJournalClicked(journals.get(position), position);
//                }
//            });
//    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, final int position) {
        Journal journal = journals.get(position);
        holder.setNote(journal);

        if (journal.getImagePath() != null && !journal.getImagePath().isEmpty()) {
            // Convert the image path string to a Uri object
            Uri imageUri = Uri.parse(journal.getImagePath());
            // Set the image URI to the ImageView
            holder.imageJournal.setImageURI(imageUri);
        }

        holder.layoutJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journalsListeners.onJournalClicked(journals.get(position), position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return journals.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }



    static class JournalViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutJournal;
        RoundedImageView imageJournal;


        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutJournal = itemView.findViewById(R.id.layoutNote);
            imageJournal = itemView.findViewById(R.id.imageJournal);

        }

        void setNote(Journal journal){
            textTitle.setText(journal.getTitle());
            if(journal.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }
            else{
                textSubtitle.setText(journal.getSubtitle());
            }
            textDateTime.setText(journal.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable)  layoutJournal.getBackground();
            if(journal.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(journal.getColor()));
            }
            else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if(journal.getImagePath() != null){
                imageJournal.setImageBitmap(BitmapFactory.decodeFile(journal.getImagePath()));
                imageJournal.setVisibility(View.VISIBLE);
            }
            else{
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
                    journals = journalsSource;
                }else {
                    ArrayList<Journal> temp = new ArrayList<>();
                    for(Journal journal : journalsSource){
                        if(journal.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || journal.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase())
                            || journal.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(journal);
                        }
                    }
                    journals = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run(){
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }

    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }
}
