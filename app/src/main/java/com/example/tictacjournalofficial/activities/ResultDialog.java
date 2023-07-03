package com.example.tictacjournalofficial.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tictacjournalofficial.R;

public class ResultDialog extends Dialog {
    private final String message;
    private final TicTacToe ticTacToe;
    public ResultDialog(@NonNull Context context, String message, TicTacToe ticTacToe) {
        super(context);
        this.message = message;
        this.ticTacToe = ticTacToe;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_dialog);
        TextView messageText = findViewById(R.id.messageText);
        Button startAgainButton = findViewById(R.id.startAgainButton);
        messageText.setText(message);
        startAgainButton.setOnClickListener(view -> {
            ticTacToe.restartMatch();
            dismiss();
        });
    }
}