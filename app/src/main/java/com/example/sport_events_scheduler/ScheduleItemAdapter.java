package com.example.sport_events_scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ScheduleItemAdapter extends ArrayAdapter<Event> {

    private Context context;
    private int resource;

    public ScheduleItemAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvLocation;
        TextView tvTime;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event item = getItem(position);

        String name = item.getName();
        String location = item.getLocation();
        String time = item.getStart();


        ViewHolder holder;
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView.findViewById(R.id.name);
            holder.tvLocation = (TextView) convertView.findViewById(R.id.location);
            holder.tvTime = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(name);
        holder.tvLocation.setText(location);
        holder.tvTime.setText(time);

        return convertView;
    }
}

