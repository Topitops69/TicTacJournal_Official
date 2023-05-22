package com.example.tictacjournalofficial.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictacjournalofficial.R;

import java.util.ArrayList;

public class Password extends AppCompatActivity implements View.OnClickListener {

    View view_01, view_02, view_03, view_04;


    RelativeLayout setBlank;
    Button btn_01, btn_02, btn_03, btn_04, btn_05, btn_06, btn_07, btn_08, btn_09, btn_0, btnClear, btnConfirm, btnSkip, btnContinue;

    ArrayList<String> numList = new ArrayList<>();
    String passCode = "";
    String num1, num2, num3, num4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initializeComponents();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnContinue = findViewById(R.id.btnContinue);
        btnSkip = findViewById(R.id.btnSkip);
        setBlank = findViewById(R.id.blankPass);

        // Check if passcode is set, if it is then hide skip and continue buttons
        if (getPassCode().length() > 0) {
            setBlank.setVisibility(View.GONE);
        }

        btnSkip.setOnClickListener(view -> startActivity(new Intent(Password.this, Theme.class)));

        btnContinue.setOnClickListener(view -> {
            if (numList.size() == 4) {
                passCode = num1 + num2 + num3 + num4;
                if (getPassCode().length() == 0) {
                    savePassCode(passCode);
                    Toast.makeText(this, "Passcode set. Please input passcode again.", Toast.LENGTH_SHORT).show();
                    numList.clear();
                    passNumber(numList);
                } else {
                    matchPassCode();
                }
            } else {
                Toast.makeText(this, "Enter 4 digit passcode", Toast.LENGTH_SHORT).show();
            }
        });

        TextView resetPass = findViewById(R.id.resetPass);
        resetPass.setOnClickListener(v -> resetPassCode());
    }



    private void initializeComponents() {
        view_01 = findViewById(R.id.view_01);
        view_02 = findViewById(R.id.view_02);
        view_03 = findViewById(R.id.view_03);
        view_04 = findViewById(R.id.view_04);

        btn_0 = findViewById(R.id.btn_0);
        btn_01 = findViewById(R.id.btn_01);
        btn_02 = findViewById(R.id.btn_02);
        btn_03 = findViewById(R.id.btn_03);
        btn_04 = findViewById(R.id.btn_04);
        btn_05 = findViewById(R.id.btn_05);
        btn_06 = findViewById(R.id.btn_06);
        btn_07 = findViewById(R.id.btn_07);
        btn_08 = findViewById(R.id.btn_08);
        btn_09 = findViewById(R.id.btn_09);
        btnClear = findViewById(R.id.btnClear);
        btnConfirm = findViewById(R.id.btnConfirm);

        btn_0.setOnClickListener(this);
        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        btn_03.setOnClickListener(this);
        btn_04.setOnClickListener(this);
        btn_05.setOnClickListener(this);
        btn_06.setOnClickListener(this);
        btn_07.setOnClickListener(this);
        btn_08.setOnClickListener(this);
        btn_09.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                numList.add("0");
                passNumber(numList);
                break;
            case R.id.btn_01:
                numList.add("1");
                passNumber(numList);
                break;
            case R.id.btn_02:
                numList.add("2");
                passNumber(numList);
                break;
            case R.id.btn_03:
                numList.add("3");
                passNumber(numList);
                break;
            case R.id.btn_04:
                numList.add("4");
                passNumber(numList);
                break;
            case R.id.btn_05:
                numList.add("5");
                passNumber(numList);
                break;
            case R.id.btn_06:
                numList.add("6");
                passNumber(numList);
                break;
            case R.id.btn_07:
                numList.add("7");
                passNumber(numList);
                break;
            case R.id.btn_08:
                numList.add("8");
                passNumber(numList);
                break;
            case R.id.btn_09:
                numList.add("9");
                passNumber(numList);
                break;
            case R.id.btnClear:
                numList.clear();
                passNumber(numList);
                break;
            case R.id.btnConfirm:
                if (numList.size() == 4) {
                    passCode = num1 + num2 + num3 + num4;
                    if (getPassCode().length() == 0) {
                        savePassCode(passCode);
                        Toast.makeText(this, "Passcode set. Please input passcode again.", Toast.LENGTH_SHORT).show();
                        numList.clear();
                        passNumber(numList);
                    } else {
                        matchPassCode();
                    }
                } else if (passCode.equals("reset")) { // Check if passcode is "reset"
                    resetPassCode();
                    Toast.makeText(this, "Passcode Reset. Set New Passcode.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Enter 4 digit passcode", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void confirmPassCode() {
        if (numList.size() == 4) {
            passCode = num1 + num2 + num3 + num4;
            if (getPassCode().length() == 0) {
                savePassCode(passCode);
            } else {
                matchPassCode();
            }
        } else {
            Toast.makeText(this, "Enter 4 digit passcode", Toast.LENGTH_SHORT).show();
            numList.clear(); // Clear the input
            passNumber(numList); // Update the passcode dots view
        }
    }

    private void passNumber(ArrayList<String> numList) {
        if (numList.size() == 0) {
            view_01.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_02.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
        } else {
            switch (numList.size()) {
                case 1:
                    num1 = numList.get(0);
                    view_01.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 2:
                    num2 = numList.get(1);
                    view_02.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 3:
                    num3 = numList.get(2);
                    view_03.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 4:
                    num4 = numList.get(3);
                    view_04.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
            }
        }
    }

    private void matchPassCode() {
        if (getPassCode().equals(passCode)) {
            Toast.makeText(this, "Passcode correct, proceeding...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Theme.class));
        } else {
            Toast.makeText(this, "Invalid Passcode. Try Again", Toast.LENGTH_SHORT).show();
            numList.clear(); // Clear the input
            passNumber(numList); // Update the passcode dots view
        }
    }


    private void savePassCode(String passCode) {
        SharedPreferences preferences = getSharedPreferences("Passcode pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("passcode", passCode);
        editor.apply();
    }

    private String getPassCode() {
        SharedPreferences preferences = getSharedPreferences("Passcode pref", Context.MODE_PRIVATE);
        return preferences.getString("passcode", "");
    }

    private void resetPassCode() {
        SharedPreferences preferences = getSharedPreferences("Passcode pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("passcode");
        editor.apply();
        Toast.makeText(this, "Passcode Reset. Set New Passcode.", Toast.LENGTH_SHORT).show();
        numList.clear(); // Clear the input
        passNumber(numList); // Update the passcode dots view
    }

}
