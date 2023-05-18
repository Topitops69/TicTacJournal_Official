package com.example.tictacjournalofficial.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.adapters.JournalsAdapter;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.databinding.FragmentJournal1Binding;
import com.example.tictacjournalofficial.quotes.QuotesData;
import com.example.tictacjournalofficial.quotes.QuotesList;

import java.util.ArrayList;
import java.util.List;


public class Journal1Fragment extends Fragment {
    //Journals
    public static int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    private RecyclerView journalsRecycleView;
    private List<Journal> journalList;
    private JournalsAdapter journalsAdapter;

    //Quotes
    private TextView quoteText, writerName;
    private ImageView btnCopy;
    private final List<QuotesList> qList = new ArrayList<>();

    //first quote position in array list
    //next if clicked
    private int currentQuotePosition = 0;
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


        //Quote code part
        quoteText = binding.txtQuote;
        writerName = binding.txtName;
        RelativeLayout relativeLayout = binding.relativeLayout;
        btnCopy = binding.copyBtn;

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                CharSequence label;
                ClipData clipData = ClipData.newPlainText("quote", qList.get(currentQuotePosition).getQoute()+"\nby " +qList.get(currentQuotePosition).getWriter());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(requireActivity(), "Quote copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });



        qList.addAll(QuotesData.getLifeQuotes());

      //get first quote here and set to TextView
        //default is pos 0
        setQuoteToTextView();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // your code here
                Toast.makeText(getContext(), "LinearLayout clicked", Toast.LENGTH_SHORT).show();
                //next quote, increment
                currentQuotePosition++;

                //check if more quotes are available in the list else only the first quote
                if(currentQuotePosition >= qList.size()){
                    currentQuotePosition = 0;
                }

                setQuoteToTextView();
            }
        });



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

    public void setQuoteToTextView(){
        //get quote from list from current quote position
        quoteText.setText(qList.get(currentQuotePosition).getQoute());

        //get Writer from list from currentQuoteposition
        writerName.setText(qList.get(currentQuotePosition).getWriter());

    }


}
