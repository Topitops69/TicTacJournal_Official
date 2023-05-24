package com.example.tictacjournalofficial.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tictacjournalofficial.Delete_Restore;
import com.example.tictacjournalofficial.activities.LoginAndRestore;
import com.example.tictacjournalofficial.activities.Password;
import com.example.tictacjournalofficial.activities.Theme;
import com.example.tictacjournalofficial.activities.Welcome; // Ensure you've imported your Welcome activity
import com.example.tictacjournalofficial.databinding.FragmentSettingsBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    String PublishableKey = "pk_test_51NB23dBuxFvTLNZkZ1A78U7N61IDjOhpXzu1Vm4IsxaUZ1I4IgSIKIc4ZnC7AgADVyVLTbUtdOPKhhMfqjAsfVkb00c8GsfC58";
    String SecretKey = "sk_test_51NB23dBuxFvTLNZkE1o423JlPyOtSKhfY83ukDGZDOFNtuz3TCZAZjFJdI9CqsMvCc5JX39Df9cIxNrd6hRZnBLF00Gq2ySG4y";
    String CustomerId;
    String EphericalKey;
    String ClientSecret;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        Button btnName = binding.btnName;
        Button btnTheme = binding.btnTheme;
        Button btnEmail = binding.btnEmail;
        Button btnPassword = binding.btnPassword;
        Button btnBackup = binding.btnBackup;
        Button btnPayment = binding.btnPayment;

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            CustomerId = object.getString("id");

                            Toast.makeText( getActivity(), CustomerId, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        btnPayment.setOnClickListener(v -> {

        });

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
