package com.example.sport_events_scheduler;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> venues;
    VenueOnClickListener listener;

    public VenueAdapter(Context context, ArrayList<String> venues, VenueOnClickListener listener) {
        this.context = context;
        this.venues = venues;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.venue, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String avenue = venues.get(position);
        holder.name.setText(avenue);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.displayActivity(view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameAvenueLabel);

        }
    }

    public interface VenueOnClickListener {
        void displayActivity(View view);
    }

}
