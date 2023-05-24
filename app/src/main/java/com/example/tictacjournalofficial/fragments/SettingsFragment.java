package com.example.tictacjournalofficial.fragments;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tictacjournalofficial.Delete_Restore;
import com.example.tictacjournalofficial.R;
import com.example.tictacjournalofficial.activities.LoginAndRestore;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


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
        Switch btnSwitch = binding.btnSwitch;

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

        btnPayment.setOnClickListener(v -> {
            paymentFlow();
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

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginAndRestore.class);
                startActivity(intent);
            }
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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            EphericalKey = object.getString("id");

                            //Toast.makeText( getActivity(), CustomerId, Toast.LENGTH_SHORT).show();

                            getClientSecret(CustomerId, EphericalKey);

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
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");

                            //Toast.makeText( getActivity(), ClientSecret, Toast.LENGTH_SHORT).show();


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

    private void createNotif() {
        String id = "my_channel_id_01";
        NotificationManager manager;
        try {
            manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = manager.getNotificationChannel(id);
                if (channel == null) {
                    channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);
                    //config nofication channel
                    channel.setDescription("[Channel description]");
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    manager.createNotificationChannel(channel);
                }
            }
            Intent notificationIntent = new Intent(getActivity(), NotificationActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), id)
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.book2))
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.book2))
                            .bigLargeIcon(null))
                    .setContentTitle("Tic Tac Journal")
                    .setContentText("Pag journal Na")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[]{100, 1000, 200, 340})
                    .setAutoCancel(false)//true touch on notificaiton menu dismissed, but swipe to dismiss
                    .setTicker("Notification");
            builder.setContentIntent(contentIntent);
            NotificationManagerCompat m = NotificationManagerCompat.from(getActivity());
            //id to generate new notification in list notifications menu
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            m.notify(new Random().nextInt(), builder.build());
        } catch (Exception e ) {

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Clear binding when fragment's view is destroyed to prevent memory leaks
    }


}
