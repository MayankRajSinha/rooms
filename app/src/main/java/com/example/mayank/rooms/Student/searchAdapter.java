package com.example.mayank.rooms.Student;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayank.rooms.R;
import com.example.mayank.rooms.URLclass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mayank on 1/13/2018.
 */

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder>{

    private Context mContext;
    private ArrayList arrayList;

    public searchAdapter(Context mContext, ArrayList arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;

    }

    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_layout, parent, false);
        return new searchHolder(view);
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {

        searchClass current = (searchClass) arrayList.get(position);
        holder.address.setText(current.getAddress());
        holder.mob_no.setText(("Mobile No : "+current.getMob()));
        holder.rent.setText('\u20B9'+current.getRent());
        holder.hid.setText(current.getHid());
        holder.roomId.setText(current.getRoomId());

        Picasso.with(mContext)
                .load(URLclass.GET_PIC_PATH + current.getPic())
                .into(holder.pic);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class searchHolder extends RecyclerView.ViewHolder {

        private ImageView pic;
        private TextView rent, address, mob_no, hid, roomId;
        private View v;

        public searchHolder(View itemView) {
            super(itemView);
            v = itemView;
            pic = itemView.findViewById(R.id.pic);
            rent = itemView.findViewById(R.id.rent);
            address = itemView.findViewById(R.id.address);
            mob_no = itemView.findViewById(R.id.mob_no);
            hid = itemView.findViewById(R.id.hid);
            roomId = itemView.findViewById(R.id.roomId);
        }
    }
}
