package com.hadiagdamapps.paarseh.activity.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.intro.IntroActivity;
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.activity.register.verifyphone.VerifyPhoneActivity;
import com.hadiagdamapps.paarseh.activity.step.lar_sentences.ListenAndRepeatActivity;
import com.hadiagdamapps.paarseh.activity.step.law.ListenAndWriteActivity;
import com.hadiagdamapps.paarseh.helpers.MySingleton;

import static com.hadiagdamapps.paarseh.helpers.Statics.*;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private void initialView() {

    }

    private void checkUser() {

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String phone = preferences.getString("phone", "testPhone");
        String password = preferences.getString("password", "testPhone");

        StringRequest request = new StringRequest(BASE_URL + "/getuser?phone=" + phone + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                Intent next = new Intent(SplashActivity.this, response.equals("ok") ? MainActivity.class : IntroActivity.class);
                startActivity(next);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SplashActivityCheckUser", error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void fin() {
        finish();
    }


    private void main() {
        initialView();
        checkUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        main();

        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone", "testPhone");
        editor.putString("password", "testPassword");
        editor.apply();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}