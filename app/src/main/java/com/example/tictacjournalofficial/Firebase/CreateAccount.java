package com.example.tictacjournalofficial.Firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccount extends AppCompatActivity {

    Button btnBackup, btnSign;
    EditText tfEmail, tfPassword, tfConfirmPassword;

    ProgressBar progressBar;

    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
        btnSign = findViewById(R.id.btnSign);
        loginTextView = findViewById(R.id.Login);
        progressBar = findViewById(R.id.progress_bar);



        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        btnSign.setOnClickListener(v->createAccount());


    }


    void createAccount(){
        String email = tfEmail.getText().toString();
        String password = tfPassword.getText().toString();
        String confirmPassword = tfConfirmPassword.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);
    }

    void createAccountInFirebase(String email, String password){
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccount.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //creating acc is done
                            Utility.showToast(CreateAccount.this, "Account Successfully Created. Check email for verification");

                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }else{
                            Utility.showToast(CreateAccount.this, task.getException().getLocalizedMessage());
                            changeInProgress(false);
                        }
                    }
                }
        );
    }


    boolean validateData(String email, String password, String confirmPassword){
        //validate the data that aere input by the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tfEmail.setError("Email is invalid");
            return false;
        }

        if(password.length()<6){
            tfPassword.setError("Password length is invalid");
            return false;
        }
        if(password.equals(tfConfirmPassword)){
            tfConfirmPassword.setError("Password not matched");
            return false;
        }
        return true;
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnSign.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            btnSign.setVisibility(View.VISIBLE);
        }
    }
}