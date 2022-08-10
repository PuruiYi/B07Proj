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
    ArrayList<Event> events;
    EventOnClickListener listener;


    public EventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
        this.listener = null;
    }

    public EventAdapter(Context context, ArrayList<Event> events, EventOnClickListener listener) {
        this.context = context;
        this.events = events;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Event event = events.get(position);
        holder.id.setText(event.getId());
        holder.name.setText(event.getName());
        holder.capacity.setText(Integer.toString(event.getCapacity()));
        holder.joined.setText(Integer.toString(event.getJoined()));
        holder.time.setText(event.getStart() + " - " + event.getEnd());
        holder.location.setText(event.getLocation());

        if(listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.displayActivity(view);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, capacity, joined, time, location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.eventId);
            name = itemView.findViewById(R.id.eventName);
            capacity = itemView.findViewById(R.id.eventCapacity);
            joined = itemView.findViewById(R.id.eventJoined);
            time = itemView.findViewById(R.id.eventTime);
            location = itemView.findViewById(R.id.eventLocation);
        }
    }

    public interface EventOnClickListener {
        void displayActivity(View view);
    }

}
