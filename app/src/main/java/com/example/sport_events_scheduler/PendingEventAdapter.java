package com.example.sport_events_scheduler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingEventAdapter extends RecyclerView.Adapter<PendingEventAdapter.MyViewHolder> {

    Context context;
    ArrayList<PendingEvent> pendingEventArrayList;
    OnItemClickListener listener;
    boolean existKey;

    public PendingEventAdapter(Context context, ArrayList<PendingEvent> pendingEventArrayList, OnItemClickListener listener) {
        this.context = context;
        this.pendingEventArrayList = pendingEventArrayList;
        this.listener = listener;
    }

    public PendingEventAdapter(Context context, ArrayList<PendingEvent> pendingEventArrayList, OnItemClickListener listener, boolean existKey) {
        this.context = context;
        this.pendingEventArrayList = pendingEventArrayList;
        this.listener = listener;
        this.existKey = existKey;
    }

    @NonNull
    @Override
    public PendingEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingEventAdapter.MyViewHolder holder, int position) {
        PendingEvent pendingEvent = pendingEventArrayList.get(position);
        holder.venueView.setText(pendingEvent.getLocation());
        holder.eventTypeView.setText(pendingEvent.getName());
        holder.timeView.setText(pendingEvent.getStart() + " - " + pendingEvent.getEnd());
        holder.capacityView.setText(String.valueOf(pendingEvent.getCapacity()));
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
        return pendingEventArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView venueView, eventTypeView, timeView, capacityView, optionView;
        OnItemClickListener listener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            venueView = itemView.findViewById(R.id.tvVenue);
            eventTypeView = itemView.findViewById(R.id.tvEvent);
            timeView = itemView.findViewById(R.id.tvTime);
            capacityView = itemView.findViewById(R.id.tvCapacity);
            optionView = itemView.findViewById(R.id.tvOption);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PendingEvent pendingEvent,int state); //state == 1: accept; state == 2: reject
    }
}
