package com.example.tictacjournalofficial.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.tictacjournalofficial.Firebase.Utility;
import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.entities.Journal;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addJournal extends AppCompatActivity {

    private EditText inputJournalTitle, inputJournalSubtitle, inputJournalText;
    private TextView textDateTime;
    private ImageView imageJournal;

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

    private Journal journal = new Journal();

    boolean editMode = false;
    String docId;
    private final String applicationID = "2HDEDMVLHZ";
    TextView tfLabel;

    Index index;
    Client client;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        db = FirebaseFirestore.getInstance();
        CollectionReference productRef = db.collection("journals");
        client = new Client(applicationID, "61e582e8f6bbd9f8a75679a9a9d9b30a");
        index = client.getIndex("journals");

        tfLabel = findViewById(R.id.tfLabel);

       // docId = getIntent().getStringExtra("docId");
        // Retrieve the Firestore Id from the Intent
        docId = getIntent().getStringExtra("objectID");


        if(docId!= null){
            tfLabel.setHint("Update");
            editMode = true;
            // The user is in 'edit mode', fetch the document from Firestore
            db.collection("journals").document("4hP0HPAz7ycxbQOUd7atPizq2Yj1").collection("my_journals").document(docId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();
                            Log.d(TAG, "DocumentSnapshot data: " + task.getResult());

                            if (document.exists()) {
                                // Convert the document into Journal object
                                Journal journal = document.toObject(Journal.class);
                                if (journal != null) {
                                    alreadyAvailableJournal = journal;  // The journal is available now
                                    setViewOrUpdateJournal();
                                }
                            } else {
                                Log.d(TAG, "No such document for ID: " + docId);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    });
        }


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
                setViewOrUpdateJournal();
            }
            Log.d("Debug", "isViewOrUpdate: " + isViewOrUpdate);
            Log.d("Debug", "alreadyAvailableJournal: " + alreadyAvailableJournal);
        } // Don't set alreadyAvailableJournal to null here.


        findViewById(R.id.removeImage).setOnClickListener(v -> {
            imageJournal.setImageBitmap(null);
            imageJournal.setVisibility(View.GONE);
            findViewById(R.id.removeImage).setVisibility(View.GONE);
            selectedImagePath = "";
        });

        //initialize the color here:
        initMiscellaneous();
        setSubtitleIndicatorColor();
    }

    private void setViewOrUpdateJournal() {
        // Removed the initialization here.

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

            // Your other code...
        }
    }





    private void saveJournal() {
        if (inputJournalTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Hey care to add a title?", Toast.LENGTH_SHORT).show();
            return;
        } else if (inputJournalSubtitle.getText().toString().trim().isEmpty()
                && inputJournalText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Hey, your secret is safe with me. Let's try that again ok?", Toast.LENGTH_SHORT).show();
            return;
        }

        final Journal journal;

        if (alreadyAvailableJournal != null) {
            // Updating an existing journal.
            journal = alreadyAvailableJournal;
        } else {
            // Creating a new journal.
            journal = new Journal();
            DocumentReference documentReference = Utility.getCollectionReferenceForJournals().document();
            journal.setFirestoreId(documentReference.getId());
        }

        journal.setTitle(inputJournalTitle.getText().toString());
        journal.setSubtitle(inputJournalSubtitle.getText().toString());
        journal.setNoteText(inputJournalText.getText().toString());
        journal.setDateTime(textDateTime.getText().toString());
        journal.setColor(selectedJournalColor);
        journal.setImagePath(selectedImagePath);

        if (editMode) {

            updateJournalToFirebase(journal);
        } else {

            // If you're not in edit mode, add a new journal.
            addJournalToFirebase(journal);
            //updateJournalToFirebase(journal);
        }



    }

    void updateJournalToFirebase(Journal journal) {
        DocumentReference documentReference = Utility.getCollectionReferenceForJournals().document(journal.getFirestoreId());

        documentReference.set(journal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(addJournal.this, "Journal updated successfully");
                Log.d("Debug", "Firestore operation: Success");

                // Here, 1 is the request code that you'll check in Journal1Fragment
                Intent resultIntent = new Intent();
                setResult(1, resultIntent);
                finish(); // close this activity
            } else {
                Utility.showToast(addJournal.this, "Failed while updating journal");
                Log.d("Debug", "Firestore operation: Failure");
            }
        });
    }

    void addJournalToFirebase(Journal journal) {
        DocumentReference documentReference = Utility.getCollectionReferenceForJournals().document(journal.getFirestoreId());

        documentReference.set(journal).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(addJournal.this, "Journal saved successfully");
                Log.d("Debug", "Firestore operation: Success");

                // Here, 1 is the request code that you'll check in Journal1Fragment
                Intent resultIntent = new Intent();
                setResult(1, resultIntent);
                finish(); // close this activity
            } else {
                Utility.showToast(addJournal.this, "Failed while saving journal");
                Log.d("Debug", "Firestore operation: Failure");
            }
        });
    }


    private void showDeleteJournalDialog(){
        if(dialogDeleteJournal == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(addJournal.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_journal,
                    findViewById(R.id.layoutDeleteJournalContainer)
            );
            builder.setView(view);
            dialogDeleteJournal = builder.create();
            if(dialogDeleteJournal.getWindow() != null){
                dialogDeleteJournal.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteJournal).setOnClickListener(v -> {
                // Delete the journal from Firebase
                DocumentReference documentReference = Utility.getCollectionReferenceForJournals().document(alreadyAvailableJournal.getFirestoreId());

                documentReference.delete().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Delete the journal from Algolia
                        index.deleteObjectAsync(alreadyAvailableJournal.getFirestoreId(), (jsonObject, e) -> {
                            if (e == null) {
                                Utility.showToast(addJournal.this, "Journal deleted successfully");
                                Log.d("Debug", "Firestore and Algolia operation: Success");

                                // If delete is successful, finish this activity and pass the result back to previous activity
                                Intent resultIntent = new Intent();
                                setResult(1, resultIntent);
                                finish();
                            } else {
                                Utility.showToast(addJournal.this, "Failed while deleting journal from Algolia");
                                Log.d("Debug", "Algolia operation: Failure");
                            }
                        });
                    } else {
                        Utility.showToast(addJournal.this, "Failed while deleting journal");
                        Log.d("Debug", "Firestore operation: Failure");
                    }
                });
            });

            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogDeleteJournal.dismiss());
        }

        dialogDeleteJournal.show();
    }



    //Color part code
    private void initMiscellaneous(){
        final LinearLayout  layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(v -> {
            if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(v -> {
            //Normal
            selectedJournalColor = "#333333";
            imageColor1.setImageResource(R.drawable.ic_ok2);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(v -> {
            //excited
            selectedJournalColor = "#fdbe3b";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(R.drawable.ic_grateful);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(v -> {
            //Angry
            selectedJournalColor = "#ff4842";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(R.drawable.ic_angry1);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(v -> {
            //Sad
            selectedJournalColor = "#3A52Fc";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(R.drawable.ic_sad1);
            imageColor5.setImageResource(0);
            setSubtitleIndicatorColor();
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(v -> {
            //cool
            selectedJournalColor = "#000000";
            imageColor1.setImageResource(0);
            imageColor2.setImageResource(0);
            imageColor3.setImageResource(0);
            imageColor4.setImageResource(0);
            imageColor5.setImageResource(R.drawable.ic_depressed);
            setSubtitleIndicatorColor();
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
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(v -> {
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
        });
        if(alreadyAvailableJournal != null){
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(v -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDeleteJournalDialog();
            });
        }


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

    //back code
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Go Back?")
                .setMessage("Are you sure you want to go back? Any unsaved changes will be lost.")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }

    protected void onDestroy() {
        if (dialogDeleteJournal != null && dialogDeleteJournal.isShowing()) {
            dialogDeleteJournal.dismiss();
        }
        super.onDestroy();
    }
}

