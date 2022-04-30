package com.hadiagdamapps.paarseh.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.adapters.LevelRecyclerAdapter;
import com.hadiagdamapps.paarseh.adapters.StepRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;
import com.hadiagdamapps.paarseh.models.LevelRecyclerModel;
import com.hadiagdamapps.paarseh.models.StepRecyclerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView levelRecycler;


    private void initialRecycler() {
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String phone = preferences.getString("phone", "");
        String password = preferences.getString("password", "");
        StringRequest request = new StringRequest(Statics.BASE_URL + "getData?phone=testPhone&password=testPassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                if (response.equals("user not found")) {
                    Statics.backToLogin(MainActivity.this);
                }

                try {

                    JSONArray levels = new JSONArray(response);

                    ArrayList<LevelRecyclerModel> list = new ArrayList<>();
                    for (int i = 0; i < levels.length(); i++) {
                        JSONObject level = levels.getJSONObject(i);


                        ArrayList<StepRecyclerModel> steps = new ArrayList<>();
                        JSONArray stepsJson = level.getJSONArray("steps");
                        for (int j = 0; j < stepsJson.length(); j++) {
                            JSONObject step = stepsJson.getJSONObject(j);
                            StepRecyclerModel model = new StepRecyclerModel(Integer.parseInt(step.getString("id")), step.getString("name"), step.getString("description"), !step.getBoolean("access"), 0);
                            steps.add(model);
                        }

                        StepRecyclerAdapter adapter = new StepRecyclerAdapter(steps, MainActivity.this);
                        LevelRecyclerModel model = new LevelRecyclerModel(Integer.parseInt(level.getString("id")), level.getString("name"), adapter);
                        list.add(model);
                    }
                    LevelRecyclerAdapter adapter = new LevelRecyclerAdapter(MainActivity.this, list);
                    levelRecycler.setAdapter(adapter);

                    SharedPreferences p = getSharedPreferences("data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = p.edit();
                    editor.putString("json", response);
                    editor.apply();

                } catch (Exception ex) {
                    Log.e("error", ex.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }



    private void initialView() {
        levelRecycler = findViewById(R.id.levelRecycler);
        levelRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void main() {
        initialView();
        initialRecycler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main();
    }
}