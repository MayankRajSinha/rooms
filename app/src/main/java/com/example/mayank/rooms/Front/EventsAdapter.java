package com.example.mayank.rooms.Front;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayank.rooms.R;

import java.util.ArrayList;

/**
 * Created by mayan on 9/28/2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.eventHolder> {

    ArrayList<eventContainer> arrayList;
    Context context;

    public EventsAdapter(ArrayList arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public eventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_cardview, parent, false);
        eventHolder holder = new eventHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(eventHolder holder, int position) {
        eventContainer current = arrayList.get(position);
        holder.header.setText(current.getTitle());
        holder.detail.setText(current.getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class eventHolder extends RecyclerView.ViewHolder {

        TextView header, detail;
        ImageView imageView;

        public eventHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.event_title);
            detail = (TextView) itemView.findViewById(R.id.event_details);
            imageView = (ImageView) itemView.findViewById(R.id.event_image);
        }
    }
}
