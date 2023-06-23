package com.example.tictacjournalofficial.fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.example.tictacjournalofficial.Firebase.Utility;
import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.addJournal;
import com.example.tictacjournalofficial.adapters.JournalsAdapter;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.databinding.FragmentJournal1Binding;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.listeners.JournalsListeners;
import com.example.tictacjournalofficial.quotes.QuotesData;
import com.example.tictacjournalofficial.quotes.QuotesList;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Journal1Fragment extends Fragment implements JournalsListeners {
    //Journals
    //Request code to  add a new journal
    public static final int REQUEST_CODE_ADD_JOURNAL_NOTE = 1;
    //Request to update a journal
    public static final int REQUEST_CODE_UPDATE_JOURNAL = 2;
    //request to show an updated journal
    public static final int REQUEST_CODE_SHOW_JOURNALS = 3;

    private RecyclerView journalsRecycleView;
    private List < Journal > journalList;
    private JournalsAdapter journalsAdapter;

    private int journalClickedPosition = -1;

    //Quotes
    private TextView quoteText, writerName;
    private ImageView btnCopy;

    EditText inputSearch;
    private final List < QuotesList > qList = new ArrayList < > ();

    //first quote position in array list
    //next if clicked
    private int currentQuotePosition = 0;

    //Algolia
    private String applicationID = "2HDEDMVLHZ";
    private String apiKey = "5d443008cfff038c4c9138511dc11a53";

    //bottom nav
    private BottomNavigationView bottomNavigationView;

    //Firebase reference Algolia:

    Journal journal;
    FirebaseFirestore db;
    Index index;
    Client client;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initializations
        // Inflate the layout for this fragment through the binding object
        FragmentJournal1Binding binding = FragmentJournal1Binding.inflate(inflater, container, false);

        journalsRecycleView = binding.journalsRecyclerView;

        // Your query
        com.google.firebase.firestore.Query query = Utility.getCollectionReferenceForJournals().orderBy("dateTime", com.google.firebase.firestore.Query.Direction.DESCENDING);
        FirestoreRecyclerOptions < Journal > options = new FirestoreRecyclerOptions.Builder < Journal > ()
                .setQuery(query, Journal.class).build();
        // Your bindings...
        journalsAdapter = new JournalsAdapter(options, this);
        journalsRecycleView.setAdapter(journalsAdapter);

        journalsRecycleView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        journalList = new ArrayList < > ();
        journalsAdapter = new JournalsAdapter(options, this);
        journalsRecycleView.setAdapter(journalsAdapter);

        getJournals(REQUEST_CODE_SHOW_JOURNALS, false);

        //search
        inputSearch = binding.inputSearch;
        // Find the bottom navigation view in the activity layout
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);

        //Quote code part
        quoteText = binding.txtQuote;
        writerName = binding.txtName;
        RelativeLayout relativeLayout = binding.relativeLayout;
        btnCopy = binding.copyBtn;
        //getters

        db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("journals");
        client = new Client(applicationID, "61e582e8f6bbd9f8a75679a9a9d9b30a");
        index = client.getIndex("journals");


        sendFirestoreDataToAlgolia();



        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This method is called to notify you that, within s, the count characters
                // beginning at start are about to be replaced by new text with length after.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This method is called to notify you that, within s, the count characters
                // beginning at start have just replaced old text that had length before.
                journalsAdapter.searchJournals(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This method is called to notify you that, somewhere within s, the text has been changed.
                if(s.toString().isEmpty()) {
                    getJournals(REQUEST_CODE_SHOW_JOURNALS, false); // show all the journals when search field is empty
                }
            }
        });

        inputSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus || !inputSearch.getText().toString().isEmpty()) {
                    relativeLayout.setVisibility(View.GONE);
                } else {

                    relativeLayout.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    inputSearch.clearFocus(); // Add this line to remove focus
                }
            }
        });

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

                currentQuotePosition++;

                //check if more quotes are available in the list else only the first quote
                if (currentQuotePosition >= qList.size()) {
                    currentQuotePosition = 0;
                }

                setQuoteToTextView();
            }
        });

        final View rootView = binding.getRoot();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            boolean wasOpen = false;

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = rootView.getRootView().getHeight();
                boolean isOpen = ((double)(screenHeight - (r.bottom - r.top)) / screenHeight) > 0.25;

                if (wasOpen && !isOpen) {
                    // The keyboard was open but it's not anymore.
                    inputSearch.clearFocus();
                }

                wasOpen = isOpen;
            }
        });

        // Return the root view of the binding object
        return binding.getRoot();
    }

    private void sendFirestoreDataToAlgolia() {
        db.collectionGroup("my_journals")  // change to collectionGroup from collection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<JSONObject> productList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                data.put("objectID", document.getId()); // Algolia requires an objectID field

                                // You can use Gson or another JSON library to convert the Map to a JSONObject.
                                // Note that this requires adding Gson to your dependencies.
                                JSONObject jsonObject = new JSONObject(data);

                                productList.add(jsonObject);
                            }

                            // Now we have a list of all our Firestore documents as JSONObjects.
                            // We can add this list to Algolia.
                            index.addObjectsAsync(new JSONArray(productList), new CompletionHandler() {
                                @Override
                                public void requestCompleted(JSONObject content, AlgoliaException error) {
                                    if (error != null) {
                                        Log.e("AlgoliaError", "Error while adding: " + error.getMessage());
                                    } else {
                                        Log.i("AlgoliaSuccess", "Data successfully added to Algolia");
                                    }
                                }
                            });
                        } else {
                            Log.d("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    // Method to save a journal to the index

    @Override
    public void onResume() {
        super.onResume();

        // This will refresh the journals every time the fragment is resumed.
        getJournals(REQUEST_CODE_SHOW_JOURNALS, false);
    }

    private void getJournals(final int requestCode, final boolean isJournalDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetJournalsTask extends AsyncTask < Void, Void, List < Journal >> {

            @Override
            protected List < Journal > doInBackground(Void...voids) {
                return JournalsDatabase.getDatabase(requireActivity()).journalDao().getAllJournals();

            }
            @Override
            protected void onPostExecute(List < Journal > journals) {
                super.onPostExecute(journals);

                if (requestCode == REQUEST_CODE_SHOW_JOURNALS) {
                    journalList.clear(); // clear the existing list
                    journalList.addAll(journals); // add all the new data
                    journalsAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_JOURNAL_NOTE) {
                    journalList.clear();
                    journalList.addAll(journals);
                    journalsAdapter.notifyDataSetChanged();
                    journalsRecycleView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_JOURNAL) {
          /*
              if (request code)remove journal from the list first then check if na delete ba or wala
              if yes kay i update, else i update natu to that same position where we removed it
           */
                    journalList.clear();
                    journalList.addAll(journals);
                    journalsAdapter.notifyDataSetChanged();
                    if (isJournalDeleted) {
                        journalsAdapter.notifyItemRemoved(journalClickedPosition);
                    } else {
                        journalList.add(journalClickedPosition, journals.get(journalClickedPosition));
                        //journalsAdapter.notifyItemChanged(journalClickedPosition);
                        journalsAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
        new GetJournalsTask().execute();

    }

    @Override
    public void onJournalClicked(Journal journal, int position) {
        journalClickedPosition = position;
        Intent intent = new Intent(requireActivity(), addJournal.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("journal", journal);
        journalActivityResultLauncher.launch(intent);
    }

    public void setQuoteToTextView() {
        //get quote from list from current quote position
        quoteText.setText(qList.get(currentQuotePosition).getQoute());

        //get Writer from list from currentQuoteposition
        writerName.setText(qList.get(currentQuotePosition).getWriter());

    }

    public EditText getInputSearch() {
        return inputSearch;
    }

    @Override
    public void onStart() {
        super.onStart();
        journalsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (journalsAdapter != null) {
            journalsAdapter.stopListening();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
        }
    }

    // Create an instance of the contract for onActivityResult
    ActivityResultLauncher < Intent > journalActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Here, we check the result returned from launched activity.
                if (result.getResultCode() == android.app.Activity.RESULT_OK) {
                    // Now checking whether it was a new journal or updated one.
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("isNoteAdded")) {
                        // New journal was added.
                        getJournals(REQUEST_CODE_ADD_JOURNAL_NOTE, false);
                    } else if (data != null && data.hasExtra("isNoteUpdated")) {
                        // Existing journal was updated.
                        getJournals(REQUEST_CODE_UPDATE_JOURNAL, data.getBooleanExtra("isNoteDeleted", false));
                    }
                }
            });

}