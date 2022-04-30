package com.hadiagdamapps.paarseh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadiagdamapps.paarseh.R;

import java.util.ArrayList;

public class SelectPracticeRecyclerAdapter extends RecyclerView.Adapter<SelectPracticeRecyclerAdapter.Holder> {

    public final ArrayList<Item> list;
    public final Context self;

    public SelectPracticeRecyclerAdapter(ArrayList<Item> list, Context self) {
        this.list = list;
        this.self = self;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(self).inflate(R.layout.select_practice_item, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Item item = list.get(position);
        holder.text.setText(item.text);
        holder.icon.setImageResource(item.icon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(self, item.target);
                self.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        final TextView text;
        final ImageView icon;
        public Holder(@NonNull View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);
            this.icon = itemView.findViewById(R.id.icon);
        }
    }

    public static class Item{
        public final String text;
        public final int icon;
        public final Class target;

        public Item(String text, int icon, Class target) {
            this.text = text;
            this.icon = icon;
            this.target = target;
        }
    }


}
