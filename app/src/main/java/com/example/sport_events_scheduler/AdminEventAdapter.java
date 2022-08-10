package com.example.sport_events_scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminEventAdapter extends RecyclerView.Adapter<AdminEventAdapter.MyViewHolder> {

    Context context;
    ArrayList<Event> events;

    public AdminEventAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_event, parent, false);
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
        holder.date.setText(event.getDate());
        holder.venue.setText(event.getLocation());

        Time curTime = new Time(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()),
                                new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        Time startTime = new Time(event.getStart(), event.getDate());
        Time endTime = new Time(event.getEnd(), event.getDate());

        ImageView img = holder.itemView.findViewById(R.id.expriedImgMg);

        if (endTime.compareTo(curTime) < 0) {
            holder.itemView.setBackgroundResource(R.drawable.cardview_red_bg);
            img.setImageResource(R.drawable.expired);
        }
        else if (startTime.compareTo(curTime) < 0) {
            holder.itemView.setBackgroundResource(R.drawable.cardview_orange_bg);
            img.setImageResource(R.drawable.hourglass);
        }
        else{
            holder.itemView.setBackgroundResource(R.drawable.cardview_blue_bg);
            img.setImageResource(R.drawable.coming_soon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = holder.itemView.findViewById(R.id.delBtn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteEvent(view);
                        btn.setVisibility(View.GONE);
                    }
                });

                int v = btn.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
                btn.setVisibility(v);
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, capacity, joined, time, date, venue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.eventIdMg);
            name = itemView.findViewById(R.id.eventNameMg);
            capacity = itemView.findViewById(R.id.eventCapacityMg);
            joined = itemView.findViewById(R.id.eventJoinedMg);
            time = itemView.findViewById(R.id.eventTimeMg);
            date = itemView.findViewById(R.id.eventDate);
            venue = itemView.findViewById(R.id.eventLocationMg);

        }
    }

    private void deleteEvent(View view) {
        View parent = (View) view.getParent();
        TextView tv_id = parent.findViewById(R.id.eventIdMg);
        TextView tv_venue = parent.findViewById(R.id.eventLocationMg);
        String id = tv_id.getText().toString();
        String venue = tv_venue.getText().toString();

        DatabaseReference eventRef = new Remote().getEventRef();
        DatabaseReference accountRef = new Remote().getAccountRef();

        eventRef.child(venue).child(id).child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot username : snapshot.getChildren()) {
                    accountRef.child(username.getKey()).child("joined").child(id).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Delete event.
        eventRef.child(venue).child(id).removeValue();
    }
}
