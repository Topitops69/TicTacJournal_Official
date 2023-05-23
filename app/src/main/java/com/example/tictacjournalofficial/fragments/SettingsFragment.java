package com.example.tictacjournalofficial.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tictacjournalofficial.Delete_Restore;
import com.example.tictacjournalofficial.activities.LoginAndRestore;
import com.example.tictacjournalofficial.activities.Password;
import com.example.tictacjournalofficial.activities.Theme;
import com.example.tictacjournalofficial.activities.Welcome; // Ensure you've imported your Welcome activity
import com.example.tictacjournalofficial.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        Button btnName = binding.btnName;
        Button btnTheme = binding.btnTheme;
        Button btnEmail = binding.btnEmail;
        Button btnPassword = binding.btnPassword;
        Button btnBackup = binding.btnBackup;

        btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to start Theme class Activity
                Intent intent = new Intent(getActivity(), Theme.class);
                startActivity(intent);
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to start Welcome class Activity
                Intent intent = new Intent(getActivity(), Welcome.class);
                startActivity(intent);
            }
        });

        btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Password.class);
                startActivity(intent);
            }
        });

        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Delete_Restore.class);
                startActivity(intent);
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginAndRestore.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding when fragment's view is destroyed to prevent memory leaks
    }
}
