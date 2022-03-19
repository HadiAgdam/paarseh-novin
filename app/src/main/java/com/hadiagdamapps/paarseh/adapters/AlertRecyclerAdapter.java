package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;

import java.util.ArrayList;

public class AlertRecyclerAdapter extends RecyclerView.Adapter<AlertRecyclerAdapter.Holder>{

    private Context self;
    private ArrayList<String> items;

    public AlertRecyclerAdapter(Context self, ArrayList<String> items){
        this.self = self;
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(self).inflate(R.layout.alert_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.text.setText(items.get(position));
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.self.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class Holder extends RecyclerView.ViewHolder{
        private TextView text;
        private View self;
        private TextView close;
        public Holder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.titleText);
            self = itemView;
            close = itemView.findViewById(R.id.closeText);
        }
    }

}
