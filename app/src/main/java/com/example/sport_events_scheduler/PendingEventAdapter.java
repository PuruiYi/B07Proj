package com.example.sport_events_scheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingEventAdapter extends RecyclerView.Adapter<PendingEventAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> pendingEvents;
    OnItemClickListener listener;

    public PendingEventAdapter(Context context, ArrayList<Event> pendingEvents, OnItemClickListener listener) {
        this.context = context;
        this.pendingEvents = pendingEvents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PendingEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingEventAdapter.MyViewHolder holder, int position) {
        Event pendingEvent = pendingEvents.get(position);
        holder.venueView.setText(pendingEvent.getLocation());
        holder.eventTypeView.setText(pendingEvent.getName());
        holder.dateView.setText(pendingEvent.getDate());
        holder.timeView.setText(pendingEvent.getStart() + " - " + pendingEvent.getEnd());
        holder.capacityView.setText(String.valueOf(pendingEvent.getCapacity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailPendingEvent.class);
                intent.putExtra("DETAIL",  pendingEvents.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });

        holder.optionView.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,holder.optionView);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item->{
                if(item.getItemId() == R.id.accept_option){
                    listener.onItemClick(pendingEvent,1);
                }else if(item.getItemId() == R.id.reject_option){
                    listener.onItemClick(pendingEvent,2);
                }else{}
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return pendingEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView venueView, eventTypeView, timeView, capacityView, optionView, dateView;
        OnItemClickListener listener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            venueView = itemView.findViewById(R.id.tvVenue);
            eventTypeView = itemView.findViewById(R.id.tvEvent);
            dateView = itemView.findViewById(R.id.tvDate);
            timeView = itemView.findViewById(R.id.tvTime);
            capacityView = itemView.findViewById(R.id.tvCapacity);
            optionView = itemView.findViewById(R.id.tvOption);

            itemView.setBackgroundResource(R.drawable.cardview_purple_bg);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Event pendingEvent,int state); //state == 1: accept; state == 2: reject
    }
}
