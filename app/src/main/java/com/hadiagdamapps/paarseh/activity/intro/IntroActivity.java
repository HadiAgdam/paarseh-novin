package com.hadiagdamapps.paarseh.activity.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.login.LoginActivity;
import com.hadiagdamapps.paarseh.activity.register.RegisterActivity;

public class IntroActivity extends AppCompatActivity {

    private Button registerButton;
    private ConstraintLayout loginBox;

    View.OnClickListener registerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    };

    View.OnClickListener loginBoxListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private void initialView(){
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerButtonListener);
        loginBox = findViewById(R.id.loginBox);
        loginBox.setOnClickListener(loginBoxListener);
    }

    private void main(){
        initialView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        main();
    }
}