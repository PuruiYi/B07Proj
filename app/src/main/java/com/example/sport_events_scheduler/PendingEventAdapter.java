package com.example.sport_events_scheduler;

import android.content.Context;
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

    public PendingEventAdapter(Context context, ArrayList<Event> pendingEvents) {
        this.context = context;
        this.pendingEvents = pendingEvents;
    }

    @NonNull
    @Override
    public PendingEventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingEventAdapter.MyViewHolder holder, int position) {
        Event pendingEvent = pendingEvents.get(position);
        holder.venueView.setText(pendingEvent.getLocation());
        holder.eventTypeView.setText(pendingEvent.getName());
        holder.timeView.setText(pendingEvent.getStart() + " - " + pendingEvent.getEnd());
        holder.capacityView.setText(String.valueOf(pendingEvent.getCapacity()));
        holder.optionView.setOnClickListener(v->{
            PopupMenu popupMenu = new PopupMenu(context,holder.optionView);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item->{
                if(item.getItemId() == R.id.accept_option){
//                    Intent intent = new Intent(context,PendingEventFragment.class);
//                    //TODO: add event to firebase database event reference
//                    /*code start here...
//                    */
//                    intent.putExtra("EVENT", (Serializable) pendingEvent);
//                    context.startActivity(intent);



                }else if(item.getItemId() == R.id.reject_option){

                }else{
                }
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

        TextView venueView, eventTypeView, timeView, capacityView, optionView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            venueView = itemView.findViewById(R.id.tvVenue);
            eventTypeView = itemView.findViewById(R.id.tvEvent);
            timeView = itemView.findViewById(R.id.tvTime);
            capacityView = itemView.findViewById(R.id.tvCapacity);
            optionView = itemView.findViewById(R.id.tvOption);
        }
    }
}
