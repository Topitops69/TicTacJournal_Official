package com.example.tictacjournalofficial.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tictacjournalofficial.Firebase.CreateAccount;
import com.example.tictacjournalofficial.activities.NotificationActivity;
import com.example.tictacjournalofficial.activities.Password;
import com.example.tictacjournalofficial.activities.Theme;
import com.example.tictacjournalofficial.activities.Welcome; // Ensure you've imported your Welcome activity
import com.example.tictacjournalofficial.databinding.FragmentSettingsBinding;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    String PublishableKey = "pk_test_51NB23dBuxFvTLNZkZ1A78U7N61IDjOhpXzu1Vm4IsxaUZ1I4IgSIKIc4ZnC7AgADVyVLTbUtdOPKhhMfqjAsfVkb00c8GsfC58";
    String SecretKey = "sk_test_51NB23dBuxFvTLNZkE1o423JlPyOtSKhfY83ukDGZDOFNtuz3TCZAZjFJdI9CqsMvCc5JX39Df9cIxNrd6hRZnBLF00Gq2ySG4y ";
    String CustomerId;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        Button btnName = binding.btnName;
        Button btnTheme = binding.btnTheme;
        Button btnEmail = binding.btnEmail;
        Button btnPassword = binding.btnPassword;
        Button btnPayment = binding.btnPayment;
       // Switch btnSwitch = binding.btnSwitch;
        Button btnReminder = binding.btnNotification;

        PaymentConfiguration.init(getActivity(), PublishableKey);

        paymentSheet = new PaymentSheet(this, this::onPaymentResult);

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            CustomerId = object.getString("id");

                            //Toast.makeText( getActivity(), CustomerId, Toast.LENGTH_SHORT).show();

                            //getEmphericalKey();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }
        };

        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //requestQueue.add(request);

/*
        btnSwitch.setOnClickListener(v -> {
            Timer timer = new Timer();
            if (btnSwitch.isChecked()) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        createNotif();
                        if(!btnSwitch.isChecked()) timer.cancel();
                    }
                },0,5000);
            } else {
                Toast.makeText(getActivity(), "Omsim", Toast.LENGTH_SHORT).show();
                timer.cancel();
            }
        });
*/
        btnPayment.setOnClickListener(v -> {
            paymentFlow();
        });

        btnTheme.setOnClickListener(v -> {
            // Create Intent to start Theme class Activity
            Intent intent = new Intent(getActivity(), Theme.class);
            startActivity(intent);
        });

        btnName.setOnClickListener(v -> {
            // Create Intent to start Welcome class Activity
            Intent intent = new Intent(getActivity(), Welcome.class);
            startActivity(intent);
        });

        btnPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Password.class);
            startActivity(intent);
        });

        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateAccount.class);
            startActivity(intent);
        });

        btnReminder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Tic Tac Journal", new PaymentSheet.CustomerConfiguration(
                CustomerId,
                EphericalKey
        )));
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(getActivity(), "Payment Success ", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEmphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);

                        EphericalKey = object.getString("id");

                        //Toast.makeText( getActivity(), CustomerId, Toast.LENGTH_SHORT).show();

                        getClientSecret(CustomerId, EphericalKey);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };

        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //requestQueue.add(request);
    }

    private void getClientSecret(String customerId, String ephericalKey) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);

                        ClientSecret = object.getString("client_secret");

                        //Toast.makeText( getActivity(), ClientSecret, Toast.LENGTH_SHORT).show();


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", "100" + "00");
                params.put("currency", "USD");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding when fragment's view is destroyed to prevent memory leaks
    }


}
