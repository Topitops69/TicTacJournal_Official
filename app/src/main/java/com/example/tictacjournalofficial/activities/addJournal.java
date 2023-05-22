package com.example.tictacjournalofficial.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.database.JournalsDatabase;
import com.example.tictacjournalofficial.entities.Journal;
import com.example.tictacjournalofficial.quotes.QuotesList;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addJournal extends AppCompatActivity {
    private EditText inputJournalTitle, inputJournalSubtitle, inputJournalText;
    private TextView textDateTime;
    private ImageView imageJournal;

    private final List<QuotesList> quotesListList = new ArrayList<>();
    //color
    private String selectedJournalColor;
    private String selectedImagePath;
    private View viewSubtitleIndicator;
    //image
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Journal alreadyAvailableJournal;

    //For depreciated API
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> selectImageLauncher;

    //alert
    private AlertDialog dialogDeleteJournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        // Initialize the requestPermissionLauncher
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                selectImageLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
            } else {
                Toast.makeText(this, "Permission to read external storage denied. Unable to add images.", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize the selectImageLauncher
        selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getInputStreamFromUri(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageJournal.setImageBitmap(bitmap);
                        imageJournal.setVisibility(View.VISIBLE);

                        selectedImagePath = selectedImageUri.toString(); // We are saving uri string instead of file path
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        // Initialize the UI components
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        inputJournalTitle = findViewById(R.id.inputJournal); //title ni siya
        inputJournalSubtitle = findViewById(R.id.inputSubtitle); //iyang subtitle
        inputJournalText = findViewById(R.id.inputJournalNote);//iyang note
        textDateTime = findViewById(R.id.textDateTime);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageJournal = findViewById(R.id.imageJournal);

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(v ->  saveJournal());

        //default color
        selectedJournalColor ="#333333";

        //initialize image here
        selectedImagePath = "";

        Intent intent = getIntent();
        if (intent.hasExtra("isViewOrUpdate")) {
            boolean isViewOrUpdate = intent.getBooleanExtra("isViewOrUpdate", false);
            if (isViewOrUpdate) {
                alreadyAvailableJournal = (Journal) intent.getSerializableExtra("journal");
                // Set the journal details in the EditText fields
                inputJournalTitle.setText(alreadyAvailableJournal.getTitle());
                inputJournalSubtitle.setText(alreadyAvailableJournal.getSubtitle());
                inputJournalText.setText(alreadyAvailableJournal.getNoteText());
                textDateTime.setText(alreadyAvailableJournal.getDateTime());

                setViewOrUpdateJournal();
            }
        }

        findViewById(R.id.removeImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageJournal.setImageBitmap(null);
                imageJournal.setVisibility(View.GONE);
                findViewById(R.id.removeImage).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        //initialize the color here:
        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void setViewOrUpdateJournal() {
        if (alreadyAvailableJournal != null) {
            inputJournalTitle.setText(alreadyAvailableJournal.getTitle());
            inputJournalSubtitle.setText(alreadyAvailableJournal.getSubtitle());
            inputJournalText.setText(alreadyAvailableJournal.getNoteText());
            textDateTime.setText(alreadyAvailableJournal.getDateTime());

            if (alreadyAvailableJournal.getImagePath() != null && !alreadyAvailableJournal.getImagePath().trim().isEmpty()){
                selectedImagePath = alreadyAvailableJournal.getImagePath(); // Set the selectedImagePath here

                try {
                    InputStream inputStream = getContentResolver().openInputStream(Uri.parse(alreadyAvailableJournal.getImagePath()));
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageJournal.setImageBitmap(bitmap);
                    imageJournal.setVisibility(View.VISIBLE);
                    findViewById(R.id.removeImage).setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to load image", Toast.LENGTH_SHORT).show();
                }
            }

            if (alreadyAvailableJournal.getColor() != null && !alreadyAvailableJournal.getColor().trim().isEmpty()) {
                selectedJournalColor = alreadyAvailableJournal.getColor();
            }
        }
    }




    private void saveJournal(){
        if(inputJournalTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Hey care to add a title?", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (inputJournalSubtitle.getText().toString().trim().isEmpty()
                && inputJournalText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Hey, your secret is safe with me. Let's try that again ok?", Toast.LENGTH_SHORT).show();
            return;
        }

        final Journal journal = new Journal();

        //setters
        journal.setTitle(inputJournalTitle.getText().toString());
        journal.setSubtitle(inputJournalSubtitle.getText().toString());
        journal.setNoteText(inputJournalText.getText().toString());
        journal.setDateTime(textDateTime.getText().toString());
        journal.setColor(selectedJournalColor);
        journal.setImagePath(selectedImagePath);

        //setting an id of new journal from an already available journal.
        if(alreadyAvailableJournal != null){
            journal.setId(alreadyAvailableJournal.getId());
        }
        @SuppressLint("StaticFieldLeak")
        class SaveJournalTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                JournalsDatabase.getDatabase(getApplicationContext()).journalDao().insertJournal(journal);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        }

        new SaveJournalTask().execute();

    }

    //Color part code
    private void initMiscellaneous(){
        final LinearLayout  layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#333333";
                imageColor1.setImageResource(R.drawable.ic_ok2);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#fdbe3b";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_excited);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#ff4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_angry1);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_sad1);
                imageColor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedJournalColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_cool);
                setSubtitleIndicatorColor();
            }
        });

        if(alreadyAvailableJournal != null && alreadyAvailableJournal.getColor() != null && alreadyAvailableJournal.getColor().trim().isEmpty()){
            switch (alreadyAvailableJournal.getColor()){
                case "#fdbe3b":  // feeling Very Happy
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#ff4842":  //Feeling Angry
                    layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A52Fc"://Feeling Sad
                    layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000": // feeling cool
                    layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }

        //add image code
        //add image code
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            addJournal.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImageLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                }
            }
        });
        if(alreadyAvailableJournal != null){
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteJournalDialog();
                }
            });
        }


    }

    private void showDeleteJournalDialog(){
        if(dialogDeleteJournal == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(addJournal.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_journal,
                    (ViewGroup) findViewById(R.id.layoutDeleteJournalContainer)
            );
            builder.setView(view);
            dialogDeleteJournal = builder.create();
            if(dialogDeleteJournal.getWindow() != null){
                dialogDeleteJournal.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteJournal).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteJournalTask extends AsyncTask<Void, Void, Void>{
                        @Override
                        protected Void doInBackground(Void... voids) {
                            JournalsDatabase.getDatabase(getApplicationContext()).journalDao()
                                    .deleteJournal(alreadyAvailableJournal);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteJournalTask().execute();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteJournal.dismiss();
                }
            });
        }

        dialogDeleteJournal.show();
    }

    private void setSubtitleIndicatorColor(){
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedJournalColor));
    }
    private void selectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with image selection
            selectImageLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        } else {
            // Permission not granted, request the permission
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. You can read the external storage.
            } else {
                // Permission denied. You can't read the external storage.
                Toast.makeText(this, "Permission to read external storage denied. Unable to add images.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    Log.d("Image URI", selectedImageUri.toString());
                    imageJournal.setImageURI(selectedImageUri);
                    imageJournal.setVisibility(View.VISIBLE);
                    findViewById(R.id.removeImage).setVisibility(View.VISIBLE);
                    selectedImagePath = selectedImageUri.toString(); // We are saving uri string instead of file path
                } else {
                    Log.e("Image URI", "Selected image URI is null");
                }
            } else {
                Log.e("Data", "Intent data is null");
            }
        }
    }
    private InputStream getInputStreamFromUri(Uri contentUri) throws FileNotFoundException {
        return getContentResolver().openInputStream(contentUri);
    }
}

