package com.example.tictacjournalofficial.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.entities.Journal;
import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsAdapter.JournalViewHolder>{

    private List<Journal> journals;

    public JournalsAdapter(List<Journal> journals){
        this.journals = journals;

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
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
            holder.setNote(journals.get(position));
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

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutJournal = itemView.findViewById(R.id.layoutNote);
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
        }
    }

}
