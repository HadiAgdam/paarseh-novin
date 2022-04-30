package com.hadiagdamapps.paarseh.activity.step;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.activity.step.lar_sentences.ListenAndRepeatActivity;
import com.hadiagdamapps.paarseh.activity.step.lar_words.ListenAndRepeatWords;
import com.hadiagdamapps.paarseh.activity.step.organize_sentences.OrganizeSentencesActivity;
import com.hadiagdamapps.paarseh.adapters.SelectPracticeRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.MySingleton;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONObject;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private Context self;
    private TextView backView;
    private TextView stepNameText;
    private RecyclerView recycler;

    private void stepNotFound() {

    }

    private void accessDenied() {

    }

    private void userNotFound() {
        Statics.backToLogin(this);
    }


    private void downloadContent(){
        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
        String phone = preferences.getString("phone", null);
        String password = preferences.getString("password", null);
        Log.e("phone", phone);
        Log.e("password", password);
//        int step_id = getIntent().getExtras().getInt("step id");
        int step_id = 1;
        if (phone == null || password == null) {
            Statics.backToLogin(this);
            finish();
        }
        StringRequest request = new StringRequest("https://hadiagdam.pythonanywhere.com/getStep?phone=" + phone + "&password=" + password + "&step_id=" + step_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.e("response of step", response);

                    if (response.equals("step not found")) stepNotFound();
                    if (response.equals("access denied")) accessDenied();
                    if (response.equals("user not found")) userNotFound();

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

    private void initialPracticeRecycler() {
        ArrayList<SelectPracticeRecyclerAdapter.Item> list = new ArrayList<>();
        list.add(new SelectPracticeRecyclerAdapter.Item("MAIN FILM", R.drawable.ic_outline_play_arrow_24, null));
        list.add(new SelectPracticeRecyclerAdapter.Item("LISTEN AND REPEAT SENTENCES", R.drawable.ic_baseline_mic_24, ListenAndRepeatActivity.class));
        list.add(new SelectPracticeRecyclerAdapter.Item("LISTEN AND REPEAT WORDS", R.drawable.ic_baseline_mic_24, ListenAndRepeatWords.class));
        list.add(new SelectPracticeRecyclerAdapter.Item("LISTEN AND WRITE", R.drawable.ic_baseline_hearing_24, ListenAndRepeatActivity.class));
        list.add(new SelectPracticeRecyclerAdapter.Item("MAKE SENTENCES", R.drawable.ic_baseline_sort_by_alpha_24, OrganizeSentencesActivity.class));

        recycler.setAdapter(new SelectPracticeRecyclerAdapter(list, this));


    }

    private void initialView() {
        recycler = findViewById(R.id.recycler);
    }

    private void main(){
        self = this;
        initialView();
        downloadContent();
        initialPracticeRecycler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        main();
    }
}