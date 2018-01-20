package com.example.mayank.rooms.Student;

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
 * Created by mayan on 1/12/2018.
 */

public class MotivationalAdapter extends RecyclerView.Adapter<MotivationalAdapter.motivationalAdapter>{


    private Context mContext;
    private ArrayList arrayList;

    public MotivationalAdapter(Context mContext, ArrayList arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;

    }
    @Override
    public motivationalAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.motivational_layout, parent, false);
        return new motivationalAdapter(view);

    }

    @Override
    public void onBindViewHolder(motivationalAdapter holder, int position) {

        MotivationalActivity.MotivationalClass current = (MotivationalActivity.MotivationalClass) arrayList.get(position);
        holder.story.setText(current.getStory());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class motivationalAdapter extends RecyclerView.ViewHolder {

        private ImageView pic;
        private TextView story;
        private View view;
        public motivationalAdapter(View itemView) {
            super(itemView);
            view = itemView;
          story = itemView.findViewById(R.id.story);
          pic = itemView.findViewById(R.id.img);
        }
    }
}
