package com.hadiagdamapps.paarseh.activity.step;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONObject;

public class StepActivity extends AppCompatActivity {

    private Context self;


    private void downloadContent(){
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String phone = preferences.getString("phone", null);
        String password = preferences.getString("password", null);
        int step_id = getIntent().getExtras().getInt("step id");
        if (phone == null || password == null) {
            Statics.backToLogin(this);
            finish();
        }
        StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/getStep?phone=" + phone + "&password=" + password + "&step_id=" + step_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    if (response.equals("user not found")) {
                        Statics.backToLogin(self);
                        finish();
                    }

                    if (response.equals("step not found") || response.equals("access denied")) {
                        finish();
                    }

                    SharedPreferences preferences1 = getSharedPreferences("step data", MODE_PRIVATE);
                    JSONObject step = new JSONObject(response);
                    SharedPreferences.Editor editor = preferences1.edit();
                    editor.putString("description", step.getString("description"));
                    editor.putString("id", step.getString("id"));
                    editor.putString("listen_repeat_sentences", step.getString("listen_repeat_sentences"));
                    editor.putString("name", step.getString("name"));
                    editor.putString("parent_id", step.getString("parent_id"));
                    editor.apply();



                } catch (Exception ex) {
                    Log.e("error", ex.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley error", error.toString());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void main(){
        self = this;
        downloadContent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        main();
    }
}