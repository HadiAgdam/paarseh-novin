package com.hadiagdamapps.paarseh.activity.step.law;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.hadiagdamapps.paarseh.R;

import java.util.ArrayList;
import java.util.Locale;

public class ListenAndWriteActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private TextView playText;
    private EditText valueText;
    private Button checkButton;
    private TextToSpeech speech;

    private String val;
    private int index = 0;
    private final ArrayList<String> sentences = new ArrayList<>();

    private void wrongAnswer() {
        int[] animations = new int[]{R.raw.angry_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length));

        Toast.makeText(this, "incorrect", Toast.LENGTH_SHORT).show();

        Log.e("random-----------", r + "");

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private void correctAnswer() {
        int[] animations = new int[]{R.raw.animated_emojis_party_emoji, R.raw.emoji_33, R.raw.grinning_face_emoji, R.raw.happy_emoji, R.raw.happy_emoji_great_work, R.raw.love_emoji, R.raw.smiley_emoji, R.raw.tbd_happyface, R.raw.winking_emoji};
        int r = (int) Math.floor(Math.random() * (animations.length));

        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.setAnimation(animations[r]);
        lottieAnimationView.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        }, 2000);


    }

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

    private void done() {
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
    }

    private void check() {
        if (trim(valueText.getText().toString()).equals(trim(val))) {
            correctAnswer();
            if (index + 1 < sentences.size()) {
                val = sentences.get(++index);

                if (index + 1 == sentences.size()) done();


            } else {
                Log.e("out of rage", "out of range");
            }
        } else {
            wrongAnswer();
        }
    }

    View.OnClickListener checkListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            check();
            valueText.setText("");
        }
    };

    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            speech.speak(val, TextToSpeech.QUEUE_FLUSH, null);
        }
    };

    private void initialView() {
        lottieAnimationView = findViewById(R.id.animation);
        playText = findViewById(R.id.speechText);
        playText.setOnClickListener(playListener);
        valueText = findViewById(R.id.valueText);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(checkListener);
    }

    private void main() {
        initialView();
        initialSpeech();
        sentences.add("Its a test");
        val = sentences.get(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_and_write);
        main();
    }
}