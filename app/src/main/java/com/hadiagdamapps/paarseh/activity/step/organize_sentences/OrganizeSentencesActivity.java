package com.hadiagdamapps.paarseh.activity.step.organize_sentences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.adapters.OrganizeRecyclerAdapter;
import com.hadiagdamapps.paarseh.adapters.OrganizeSelectedRecyclerAdapter;
import com.hadiagdamapps.paarseh.models.OrganizeRecyclerModel;
import com.hadiagdamapps.paarseh.models.OrganizeSelectedRecyclerModel;

import java.util.ArrayList;

public class OrganizeSentencesActivity extends AppCompatActivity {

    private RecyclerView selectedRecycler;
    private RecyclerView wordsRecycler;
    private final ArrayList<OrganizeRecyclerModel> wordsList = new ArrayList<>();
    private final ArrayList<OrganizeSelectedRecyclerModel> selectedList = new ArrayList<>();

    private void commit() {
        wordsRecycler.setAdapter(new OrganizeRecyclerAdapter(this, wordsList));
        selectedRecycler.setAdapter(new OrganizeSelectedRecyclerAdapter(selectedList, this));
    }

    private void selectedWordOnclick(OrganizeSelectedRecyclerModel model) {
        for (int i = 0; i < selectedList.size(); i++) {
            if (model.id == selectedList.get(i).id) {
                selectedList.remove(i);
                commit();
                return;
            }
        }
    }

    private void removeSelected(int index) {
        selectedList.remove(index);
        commit();
    }

    private void wordOnclick(OrganizeRecyclerModel model) {

        for (int i = 0; i < selectedList.size(); i++) {
            if (model.id == selectedList.get(i).id) {
                removeSelected(i);
                return;
            }
        }
        selectedList.add(new OrganizeSelectedRecyclerModel(model) {
            @Override
            public void onClick(OrganizeSelectedRecyclerModel self, TextView textView) {
                selectedWordOnclick(self);
            }
        });
        commit();
    }

    private String getData() {
        return "Its a simple text.";
    }

    private void initialWordsRecycler() {
        String[] data = getData().split(" ");

        for (int i = 0; i < data.length; i++) {
            wordsList.add(new OrganizeRecyclerModel(i, data[i]) {
                @Override
                public void onClick(OrganizeRecyclerModel self, ToggleButton toggle) {
                    wordOnclick(self);
                }
            });
        }

        wordsRecycler.setAdapter(new OrganizeRecyclerAdapter(this, wordsList));

    }

    private void initialView() {
        selectedRecycler = findViewById(R.id.selectedRecycler);
        selectedRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        wordsRecycler = findViewById(R.id.wordsRecycler);
        wordsRecycler.setLayoutManager(new FlexboxLayoutManager(this));
    }

    private void main() {
        initialView();
        initialWordsRecycler();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_sentences);
        main();
    }
}