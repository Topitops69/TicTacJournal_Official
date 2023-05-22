package com.example.tictacjournalofficial.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.TicTacToe;


public class XOXOFragment extends Fragment {

    private EditText playerOne;
    private EditText playerTwo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_x_o_x_o, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerOne = view.findViewById(R.id.playerOne);
        playerTwo = view.findViewById(R.id.playerTwo);
        Button startGameButton = view.findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(v -> {
            String getPlayerOneName = playerOne.getText().toString();
            String getPlayerTwoName = playerTwo.getText().toString();
            if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter player name", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), TicTacToe.class);
                intent.putExtra("playerOne", getPlayerOneName);
                intent.putExtra("playerTwo", getPlayerTwoName);
                startActivity(intent);
            }
        });
    }
}
