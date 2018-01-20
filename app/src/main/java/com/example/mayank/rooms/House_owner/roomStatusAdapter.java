package com.example.mayank.rooms.House_owner;

import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayank.rooms.R;

import java.util.ArrayList;

/**
 * Created by mayank on 12/26/2017.
 */

public class roomStatusAdapter extends RecyclerView.Adapter<roomStatusAdapter.roomStatusHolder>{


    private Context mContext;
    private ArrayList arrayList;

    public roomStatusAdapter(Context mContext, ArrayList arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;

    }

    @Override
    public roomStatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.room_status_layout1, parent, false);
        return new roomStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(final roomStatusHolder holder, int position) {

        roomStatusClass current = (roomStatusClass) arrayList.get(position);

        holder.rid.setText("Room Id : "+current.getRoomId());
        holder.rent.setText("Rent : "+'\u20B9'+current.getRent());
        holder.forWhom.setText("For : "+ current.getForGender());
        holder.accomodated.setText("Accomodated : "+ current.getAccomodated());
        holder.guest.setText("Guest : "+ current.getGuest());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class roomStatusHolder extends RecyclerView.ViewHolder {

        private ImageView room_pic;
        private TextView rid, rent, forWhom, accomodated, guest;

        public roomStatusHolder(View itemView) {
            super(itemView);
            room_pic = itemView.findViewById(R.id.roomPic);
            rid = itemView.findViewById(R.id.rid);
            rent = itemView.findViewById(R.id.rent);
            forWhom = itemView.findViewById(R.id.forWhom);
            accomodated = itemView.findViewById(R.id.accomodated);
            guest = itemView.findViewById(R.id.guest);
        }
    }
}
