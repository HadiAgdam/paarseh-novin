package com.hadiagdamapps.paarseh.activity.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.helpers.MySingleton;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameText, phoneText, passwordText;
    private Button registerButton;


    private void invalidPhone(){
        Toast.makeText(this, "Phone Number is used. Please enter another phone", Toast.LENGTH_LONG).show();
    }

    private void success(){

    }

    private void error(Exception ex){
        Log.e("error", ex.toString());
    }


    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/createUser?name=" + nameText.getText() + "&phone=" + phoneText.getText() + "&password=" + passwordText.getText(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success")) success();
                    else if (response.equals("phone number is used")) invalidPhone();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError exception) {
                    error(exception);
                }
            });
            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
        }
    };


    private void initialView(){
        nameText = findViewById(R.id.nameText);
        phoneText = findViewById(R.id.phoneText);
        passwordText = findViewById(R.id.passwordText);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(registerListener);
    }

    private void main(){
        initialView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        main();
    }
}