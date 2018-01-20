package com.example.mayank.rooms.House_owner;

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
 * Created by mayan on 1/11/2018.
 */

public class renterAdapter extends RecyclerView.Adapter<renterAdapter.roomStatusHolder>{


    private Context mContext;
    private ArrayList arrayList;

    public renterAdapter(Context mContext, ArrayList arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;

    }


    @Override
    public roomStatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.renters_layout, parent, false);
        return new roomStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(roomStatusHolder holder, int position) {
        renterClass current = (renterClass) arrayList.get(position);

        holder.rid.setText("Room Id : "+current.getRid());
        holder.name.setText("Name : "+current.getName());
        holder.fname.setText("Father's Name : "+current.getFname());
        holder.address.setText("Address : "+current.getAddress());
        holder.sno.setText("Student's Number : "+current.getSnumber());
        holder.fno.setText("Father's Number : "+current.getFnumber());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class roomStatusHolder extends RecyclerView.ViewHolder {

        private ImageView stu_pic;
        private TextView rid, name, fname, address, sno, fno;
        public roomStatusHolder(View itemView) {
            super(itemView);
            stu_pic = itemView.findViewById(R.id.stuPic);
            name = itemView.findViewById(R.id.name);
            rid = itemView.findViewById(R.id.rid);
            fname = itemView.findViewById(R.id.fName);
            address = itemView.findViewById(R.id.address);
            sno = itemView.findViewById(R.id.sno);
            fno = itemView.findViewById(R.id.fnumber);
        }
    }
}
