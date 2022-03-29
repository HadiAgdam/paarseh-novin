package com.hadiagdamapps.paarseh.activity.step.lar_sentences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.adapters.TextRecyclerAdapter;
import com.hadiagdamapps.paarseh.helpers.Statics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class ListenAndRepeatActivity extends AppCompatActivity {

    private TextView backTextView, speechText, micText, counterText;
    private Context self;
    private RecyclerView recycler;
    private int index = -1;
    private ArrayList<TextView> viewList;
    private String text;
    private int correct_count = 0;
    private TextToSpeech speech;


    View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener speechListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    };

    View.OnClickListener micListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
            startActivityForResult(intent, 100);
        }
    };

    private String trim(String input) {
        input = input.toLowerCase();
        input = input.trim();
        String[] bad = new String[]{"!", "\"", ".", "'", "?", "(", ")",};

        for (String b : bad) {
            input = input.replace(b, "");
        }
        Log.e("trim", input);
        return input;
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void wrongAnswer() {
        toast("wrong answer");
    }

    private void correctAnswer() {
        toast("true answer");
        correct_count++;
        String t = String.valueOf(correct_count);
        counterText.setText(t + "/ 10");
        if (correct_count == 10) {
            correct_count = 0;
            next();
        }
    }

    private void initialSpeech() {
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });
        speech.setSpeechRate(.5f);
        speech.setPitch(0);
    }

    private void next() {
        if (index != -1) {
            viewList.get(index).setTextColor(getResources().getColor(R.color.black));
        }
        index++;
        viewList.get(index).setTextColor(getResources().getColor(R.color.link_color));
        text = viewList.get(index).getText().toString();
    }

    private void initialRecycler() {
        SharedPreferences preferences = getSharedPreferences("step data", MODE_PRIVATE);
        // String data = preferences.getString("listen_repeat_sentences", null);
//        String data = "Its a simple test._You Can listen and see how does it works._\nIts the second line and last sentences.";
        String data = "some text._line two._last one";
        String[] sentences = data.split("_");

        TextRecyclerAdapter adapter = new TextRecyclerAdapter(sentences, self);
        recycler.setAdapter(adapter);
        viewList = adapter.getItems();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 1000);
    }

    private void initialView() {
        backTextView = findViewById(R.id.back);
        backTextView.setOnClickListener(backListener);
        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        speechText = findViewById(R.id.speechText);
        speechText.setOnClickListener(speechListener);
        micText = findViewById(R.id.micText);
        micText.setOnClickListener(micListener);
        counterText = findViewById(R.id.counterText);
    }

    private void main() {
        self = this;
        initialView();
        initialRecycler();
        initialSpeech();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String result = trim(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            if (result.trim().equals(trim(text))) {
                correctAnswer();
            } else {
                wrongAnswer();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_and_repeat);
        main();
    }
}