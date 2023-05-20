package com.example.tictacjournalofficial.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.example.tictacjournalofficial.activities.addJournal;
import com.example.tictacjournalofficial.adapters.JournalsAdapter;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.databinding.FragmentJournal1Binding;
import com.example.tictacjournalofficial.listeners.JournalsListeners;
import com.example.tictacjournalofficial.quotes.QuotesData;
import com.example.tictacjournalofficial.quotes.QuotesList;

import java.util.ArrayList;
import java.util.List;


public class Journal1Fragment extends Fragment implements JournalsListeners {
    //Journals
    //Request code to  add a new journal
    public static final int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    //Request to update a journal
    public static final int REQUEST_CODE_UPDATE_JOURNAL = 2;
    //request to show an updated journal
    public static final int REQUEST_CODE_SHOW_JOURNALS = 3;

    private RecyclerView journalsRecycleView;
    private List<Journal> journalList;
    private JournalsAdapter journalsAdapter;

    private int journalClickedPosition = -1;

    //Quotes
    private TextView quoteText, writerName;
    private ImageView btnCopy;
    private final List<QuotesList> qList = new ArrayList<>();

    //first quote position in array list
    //next if clicked
    private int currentQuotePosition = 0;

    //listeners

    // Create an instance of the contract for onActivityResult
    ActivityResultLauncher<Intent> journalActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Here, we check the result returned from launched activity.
                if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                    // Now checking whether it was a new journal or updated one.
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("isNoteAdded")) {
                        // New journal was added.
                        getJournals(REQUEST_CODE_ADD_JOURNAL_NOTE);
                    } else if (data != null && data.hasExtra("isNoteUpdated")) {
                        // Existing journal was updated.
                        getJournals(REQUEST_CODE_UPDATE_JOURNAL);
                    }
                }
            });


    @Override
    public void onJournalClicked(Journal journal, int position) {
        journalClickedPosition = position;
        Intent intent = new Intent(requireActivity(), addJournal.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("journal", journal);
        journalActivityResultLauncher.launch(intent);
    }

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
        journalsAdapter = new JournalsAdapter(journalList, this);
        journalsRecycleView.setAdapter(journalsAdapter);

        getJournals(REQUEST_CODE_SHOW_JOURNALS);


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
                ClipData clipData = ClipData.newPlainText("quote", qList.get(currentQuotePosition).getQoute() + "\nby " + qList.get(currentQuotePosition).getWriter());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(requireActivity(), "Quote copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //listeners


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
                if (currentQuotePosition >= qList.size()) {
                    currentQuotePosition = 0;
                }

                setQuoteToTextView();
            }
        });


        // Return the root view of the binding object
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // This will refresh the journals every time the fragment is resumed.
        getJournals(REQUEST_CODE_SHOW_JOURNALS);
    }

    private void getJournals(final int requestCode) {

        @SuppressLint("StaticFieldLeak")
        class GetJournalsTask extends AsyncTask<Void, Void, List<Journal>> {

            @Override
            protected List<Journal> doInBackground(Void... voids) {
                return JournalsDatabase.getDatabase(requireActivity()).journalDao().getAllJournals();
            }

            @Override
            protected void onPostExecute(List<Journal> journals) {
                super.onPostExecute(journals);

                if (requestCode == REQUEST_CODE_SHOW_JOURNALS) {
                    journalList.clear();  // clear the existing list
                    journalList.addAll(journals);  // add all the new data
                    journalsAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_JOURNAL_NOTE) {
                    journalList.clear();
                    journalList.addAll(journals);
                    journalsAdapter.notifyDataSetChanged();
                    journalsRecycleView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_JOURNAL) {
                    journalList.clear();
                    journalList.addAll(journals);
                    journalsAdapter.notifyDataSetChanged();
                }
            }

        }
        new GetJournalsTask().execute();

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_ADD_JOURNAL_NOTE && resultCode == RESULT_OK) {
//            if (data != null && data.hasExtra("isNoteAdded")) {
//                // New journal was added.
//                getJournals(REQUEST_CODE_ADD_JOURNAL_NOTE);
//            }
//        } else if (requestCode == REQUEST_CODE_UPDATE_JOURNAL && resultCode == RESULT_OK) {
//            if (data != null && data.hasExtra("isNoteUpdated")) {
//                // Existing journal was updated.
//                getJournals(REQUEST_CODE_UPDATE_JOURNAL);
//            }
//        }
//    }


    public void setQuoteToTextView() {
        //get quote from list from current quote position
        quoteText.setText(qList.get(currentQuotePosition).getQoute());

        //get Writer from list from currentQuoteposition
        writerName.setText(qList.get(currentQuotePosition).getWriter());

    }

}
