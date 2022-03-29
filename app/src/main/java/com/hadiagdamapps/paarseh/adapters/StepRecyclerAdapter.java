package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;
import com.hadiagdamapps.paarseh.models.StepRecyclerModel;

import java.util.ArrayList;

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.Holder> {

    private final ArrayList<StepRecyclerModel> list;
    private final Context self;

    public StepRecyclerAdapter(ArrayList<StepRecyclerModel> list, Context self){
        this.list = list;
        this.self = self;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(self).inflate(R.layout.step_recycler_item, parent, false));
    }

    private void go(int id){
        Toast.makeText(self, "id :" + id, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(self, null);
        intent.putExtra("step id", id + "");
        self.startActivity(intent);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        StepRecyclerModel model = list.get(position);
        holder.idNumberText.setText(String.valueOf(position + 1));
        holder.titleText.setText(model.name);
        holder.descriptionText.setText(model.description);
        holder.percentText.setText(String.valueOf(model.progress));
        holder.self.setOnClickListener(model.locked ? null : new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(model.id);
            }
        });
        holder.overlay.setVisibility(model.locked ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView idNumberText;
        TextView titleText;
        TextView descriptionText;
        TextView percentText;
        View self;
        View overlay;

        public Holder(@NonNull View itemView) {
            super(itemView);
            idNumberText = itemView.findViewById(R.id.idNumber);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            percentText = itemView.findViewById(R.id.percentText);
            self = itemView;
            overlay = itemView.findViewById(R.id.overlay);
        }
    }

}
