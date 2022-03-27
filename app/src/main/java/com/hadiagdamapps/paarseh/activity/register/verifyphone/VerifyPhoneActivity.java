package com.hadiagdamapps.paarseh.activity.register.verifyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.activity.register.RegisterActivity;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText codeInput;
    private Button checkButton;
    private TextView sendAgainLink, changePhoneLink;
    private String phone;
    private String password;
    private final Context self = this;

    private void success(){
        StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/getuser?phone=" + phone + "&password=" + password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject data = array.getJSONObject(0);
                    SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id", Integer.parseInt(data.getString("id")));
                    editor.putString("name", data.getString("name"));
                    editor.putString("phone", data.getString("phone"));
                    editor.putString("register_date", data.getString("register_date"));
                    editor.putString("password", data.getString("password"));
                    editor.apply();
                    Intent intent = new Intent(self, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception ex){
                    Log.e("error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        MySingleton.getInstance(self).addToRequestQueue(request);
    }


    private View.OnClickListener checkCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/checkCode/" + phone + "?code=" + codeInput.getText().toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    switch (response){

                        case "invalid phone number":
                            toast("Phone number is invalid. Please login or register.");
                            break;

                        case "wrong code":
                            break;

                        case "success":
                            toast("Login was successfully!");
                            success();
                            break;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(self).addToRequestQueue(request);
        }
    };

    private View.OnClickListener sendAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendCode();
        }
    };

    void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void sendCode() {
        StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/getVeCode?phone=" + phone, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "not found":
                        toast("phone number not found");
                        Intent intent = new Intent(self, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case "invalid":
                        toast("phone number is verified");
                        Statics.backToLogin(self);
                        finish();
                        break;

                    case "wait before send again":
                        toast("please wait before sending again");
                        break;

                    case "success":
                        setTimer();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(self).addToRequestQueue(request);
    }

    private void initialView() {
        codeInput = findViewById(R.id.codeText);
        checkButton = findViewById(R.id.checkCodeButton);
        checkButton.setOnClickListener(checkCodeListener);
        changePhoneLink = findViewById(R.id.checkCodeButton);
        sendAgainLink = findViewById(R.id.sendAgainLink);
        sendAgainLink.setOnClickListener(sendAgainListener);

    }

    private void setTimer() {
        sendAgainLink.setTextColor(Color.WHITE);
        sendAgainLink.setOnClickListener(null);
        CountDownTimer timer = new CountDownTimer(59000, 1000) {
            @Override
            public void onTick(long l) {
                codeInput.setHint(String.valueOf(Integer.parseInt(String.valueOf(l / 1000))));
            }

            @Override
            public void onFinish() {
                sendAgainLink.setTextColor(Color.parseColor("#1391E7"));
                sendAgainLink.setOnClickListener(sendAgainListener);
            }
        };
        timer.start();
    }

    private void main() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        phone = preferences.getString("phone", null);
        password = preferences.getString("password", null);
        if (phone == null || password == null){
            Statics.backToLogin(self);
            finish();
            return;
        }
        initialView();
        sendCode();
        setTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        main();
    }
}