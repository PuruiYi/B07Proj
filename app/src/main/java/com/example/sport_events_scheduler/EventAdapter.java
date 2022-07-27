package com.example.sport_events_scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    Context context;
    ArrayList<SportEvent> events;

    public EventAdapter(Context context, ArrayList<SportEvent> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        SportEvent event = events.get(position);
        holder.name.setText(event.getName());
        holder.capacity.setText(Integer.toString(event.getCapacity()));
        holder.joined.setText(Integer.toString(event.getJoined()));
        holder.start.setText(event.getStart());
        holder.end.setText(event.getEnd());
        holder.location.setText(event.getLocation());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, capacity, joined, start, end, location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.eventName);
            capacity = itemView.findViewById(R.id.eventCapacity);
            joined = itemView.findViewById(R.id.eventJoined);
            start = itemView.findViewById(R.id.eventStartAt);
            end = itemView.findViewById(R.id.eventEndAt);
            location = itemView.findViewById(R.id.eventLocation);
        }
    }

}
