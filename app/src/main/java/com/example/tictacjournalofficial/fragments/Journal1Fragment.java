package com.example.tictacjournalofficial.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tictacjournalofficial.adapters.JournalsAdapter;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.databinding.FragmentJournal1Binding;

import java.util.ArrayList;
import java.util.List;


public class Journal1Fragment extends Fragment {
    public static int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    private RecyclerView journalsRecycleView;
    private List<Journal> journalList;
    private JournalsAdapter journalsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment through the binding object
        FragmentJournal1Binding binding = FragmentJournal1Binding.inflate(inflater, container, false);

        journalsRecycleView = binding.journalsRecyclerView;
        journalsRecycleView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        journalList = new ArrayList<>();
        journalsAdapter = new JournalsAdapter(journalList);
        journalsRecycleView.setAdapter(journalsAdapter);

        getJournals();

        // Return the root view of the binding object
        return binding.getRoot();
    }

    private void getJournals(){

        @SuppressLint("StaticFieldLeak")
        class GetJournalsTask extends AsyncTask<Void, Void, List<Journal>> {

            @Override
            protected List<Journal> doInBackground(Void... voids) {
                return JournalsDatabase.getDatabase(requireActivity()).journalDao().getAllJournals();
            }

            @Override
            protected void onPostExecute(List<Journal> journals) {
                super.onPostExecute(journals);

                if(journalList.size() == 0){
                    journalList.addAll(journals);
                    journalsAdapter.notifyDataSetChanged();
                }else{
                    journalList.add(0, journals.get(0));
                    journalsAdapter.notifyItemInserted(0);
                }
                journalsRecycleView.smoothScrollToPosition(0);
            }
        }
        new GetJournalsTask().execute();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_JOURNAL_NOTE && resultCode == RESULT_OK){
            getJournals();

        }
    }
}
