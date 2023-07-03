package com.example.tictacjournalofficial.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.Welcome_end;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText tfEmail, tfPassword;

    Button btnLogin;

    ProgressBar progressBar;

    TextView tfCreateAccount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tfCreateAccount = findViewById(R.id.tfRegister);
        progressBar = findViewById(R.id.progress_bar);


        tfCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
            startActivity(intent);
        });

        
        btnLogin.setOnClickListener((v) -> loginUser());
    }

    private void loginUser() {
        String email = tfEmail.getText().toString();
        String password = tfPassword.getText().toString();

        boolean isValidated = validateData(email, password);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email,password);
    }

    private void loginAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this,
                task -> {
                    if(task.isSuccessful()){
                        //Login is successful
                        if(firebaseAuth.getCurrentUser().isEmailVerified()){
                            //go to main activity
                            startActivity(new Intent(Login.this, Welcome_end.class));
                        }else{
                            Utility.showToast(Login.this, "Email not verified, Please verify your email.");
                            changeInProgress(false);
                        }
                    }else{
                        Utility.showToast(Login.this, task.getException().getLocalizedMessage());
                        changeInProgress(false);
                    }
                }
        );
    }


    boolean validateData(String email, String password){
        //validate the data that aere input by the user

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            tfEmail.setError("Email is invalid");
            return false;
        }

        if(password.length()<6){
            tfPassword.setError("Password length is invalid");
            return false;
        }

        return true;
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        } else{
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}