package com.example.sport_events_scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> venues;

    public VenueAdapter(Context context, ArrayList<String> venues) {
        this.context = context;
        this.venues = venues;
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

}
