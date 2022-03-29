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
import com.hadiagdamapps.paarseh.helpers.MySingleton;

import static com.hadiagdamapps.paarseh.helpers.Statics.*;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {


    private void initialView(){

    }

    private boolean checkUser(String phone, String password){
        final boolean[] result = new boolean[]{false};
        StringRequest request = new StringRequest(BASE_URL + "/getuser?phone=" + phone + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                result[0] = response.equals("ok");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result[0] = false;
                Log.e("SplashActivityCheckUser", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password);
                return null;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);

        return result[0];
    }

    private void fin(){
        finish();
    }

    private void go(){
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String username = preferences.getString("phone", "testPhone");
        String password = preferences.getString("password", "testPassword");
        final Intent next = checkUser(username, password) ? null : new Intent(this, IntroActivity.class);


        new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(next);
                SplashActivity.this.finish();
                fin();
            }
        }.start();
    }

    private void main(){
        initialView();
        go();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        main();
        startActivity(new Intent(this, ListenAndRepeatActivity.class));
    }
}