package com.hadiagdamapps.paarseh.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.hadiagdamapps.paarseh.activity.main.MainActivity;
import com.hadiagdamapps.paarseh.helpers.MySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneText;
    private EditText passwordText;
    private Button loginButton;
    private RecyclerView alertRecycler;

    private void notFound(){
        Toast.makeText(this, "check username or password", Toast.LENGTH_LONG).show();
    }

    private void success(JSONObject data){
        try {
            SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("id", Integer.parseInt(data.getString("id")));
            editor.putString("name", data.getString("name"));
            editor.putString("phone", data.getString("phone"));
            editor.putString("register_date", data.getString("register_date"));
            editor.putString("password", data.getString("password"));
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }catch (Exception ex){
            Log.e("error", ex.toString());
        }
    }

    private void exception(VolleyError error){
        Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show();
    }

    private void exception(String error, Exception ex){
        Log.e("exception", ex.toString());
        Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show();
    }


    View.OnClickListener buttonLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            view.setEnabled(false);
            StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/getuser?phone=" + phoneText.getText() + "&password=" + passwordText.getText(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("response", response);
                    if (response.equals("[]")){
                        notFound();
                        return;
                    }
                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject data = array.getJSONObject(0);
                        success(data);

                    }catch (Exception ex){
                        exception(response, ex);
                    }
                    view.setEnabled(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                    exception(error);
                }
            });
            MySingleton.getInstance(LoginActivity.this).addToRequestQueue(request);
        }
    };

    private void initialView(){
        phoneText = findViewById(R.id.phoneText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.buttonRegister);
        loginButton.setOnClickListener(buttonLoginListener);
    }

    private void main(){
        initialView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        main();
    }
}