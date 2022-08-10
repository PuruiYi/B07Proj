package com.example.sport_events_scheduler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinOrQuitEventAdapter extends RecyclerView.Adapter<JoinOrQuitEventAdapter.MyViewHolder> {

    private DatabaseReference eventRef;
    private DatabaseReference accountRef;
    private Intent parent;
    private Context context;
    private ArrayList<Event> events;

    public JoinOrQuitEventAdapter(Context context, ArrayList<Event> events, Intent parent) {
        this.context = context;
        this.events = events;
        this.parent = parent;
        eventRef = new Remote().getEventRef();
        accountRef = new Remote().getAccountRef();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.join_quit_event, parent, false);
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

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinEvent(view, holder);
            }
        });

        holder.quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitEvent(view, holder);
            }
        });


        Query checkJoined = accountRef.child(parent.getStringExtra("user")).child("joined").orderByKey().equalTo(holder.id.getText().toString());
        checkJoined.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.quitBtn.setVisibility(View.VISIBLE);
                    //holder.quitBtn.setText();
                }

                else {
                    holder.joinBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, capacity, joined, time, location;
        ImageButton joinBtn, quitBtn;
        int duration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.eventId);
            name = itemView.findViewById(R.id.eventName);
            capacity = itemView.findViewById(R.id.eventCapacity);
            joined = itemView.findViewById(R.id.eventJoined);
            time = itemView.findViewById(R.id.eventTime);
            location = itemView.findViewById(R.id.eventLocation);
            joinBtn = itemView.findViewById(R.id.joinEventBtn);
            quitBtn = itemView.findViewById(R.id.quitEventBtn);

            /** Retrieve and cache the system's default "long" animation time. */
            duration = itemView.getContext().getResources().getInteger(
                    android.R.integer.config_longAnimTime);
        }
    }

    private void joinEvent(View view, MyViewHolder holder) {
        View layout = (View) view.getParent();
        String venue = ((TextView)layout.findViewById(R.id.eventLocation)).getText().toString();
        String id = ((TextView)layout.findViewById(R.id.eventId)).getText().toString();


        eventRef.child(venue).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // If the user has already joined the event.
                    for (DataSnapshot user : snapshot.child("users").getChildren()) {
                        if (user.getKey().equals(parent.getStringExtra("user"))) {
                            return;
                        }
                    }
                    // Track joined users.
                    int old = snapshot.child("joined").getValue(Integer.class);
                    int capacity = snapshot.child("capacity").getValue(Integer.class);
                    if (old != capacity) {
                        snapshot.getRef().child("joined").setValue(old + 1);
                        snapshot.getRef().child("users").child(parent.getStringExtra("user")).setValue("");
                    }
                    // Track joined event in users side.
                    accountRef.child(parent.getStringExtra("user")).child("joined").child(id).setValue(venue);
                    holder.joinBtn.setVisibility(View.GONE);
                    holder.quitBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(context.getApplicationContext(), "Joined", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }

    private void quitEvent(View view, MyViewHolder holder) {
        View layout = (View) view.getParent();
        String venue = ((TextView)layout.findViewById(R.id.eventLocation)).getText().toString();
        String id = ((TextView)layout.findViewById(R.id.eventId)).getText().toString();


        eventRef.child(venue).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // If the user has already joined the event.
                    for (DataSnapshot user : snapshot.child("users").getChildren()) {
                        if (user.getKey().equals(parent.getStringExtra("user"))) {
                            // Remove the user from the event.
                            int old = snapshot.child("joined").getValue(Integer.class);
                            snapshot.getRef().child("joined").setValue(old - 1);
                            snapshot.getRef().child("users").child(parent.getStringExtra("user")).removeValue();
                            // Track joined event in users side.
                            accountRef.child(parent.getStringExtra("user")).child("joined").child(id).removeValue();
                            holder.joinBtn.setVisibility(View.VISIBLE);
                            holder.quitBtn.setVisibility(View.GONE);
                            Toast.makeText(context.getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}
