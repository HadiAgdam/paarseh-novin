package com.hadiagdamapps.paarseh.activity.verifyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.helpers.MySingleton;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText codeInput;
    private Button checkButton;
    private TextView sendAgainLink, changePhoneLink;


    private View.OnClickListener checkCodeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private View.OnClickListener sendAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sendCode();
            setTimer();
        }
    };

    private boolean sendCode(){
        boolean[] result = new boolean[]{false};
        StringRequest request = new StringRequest("", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("ok")){
                    result[0] = true;
                }
                else if (response.equals("invalid phone.")){
                    result[0] = false;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result[0] = false;
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);

        return result[0];
    }

    private void initialView(){
        codeInput = findViewById(R.id.codeText);
        checkButton = findViewById(R.id.checkCodeButton);
        checkButton.setOnClickListener(checkCodeListener);
        changePhoneLink = findViewById(R.id.checkCodeButton);
        sendAgainLink = findViewById(R.id.sendAgainLink);
        sendAgainLink.setOnClickListener(sendAgainListener);

    }

    private void setTimer(){
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

    private void main(){
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