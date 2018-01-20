package com.example.mayank.rooms;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mayank on 1/15/2018.
 */

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.searchHolder> {

    private Context mContext;
    private ArrayList arrayList;

    public searchAdapter(Context mContext, ArrayList arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;

    }

    @Override
    public searchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.renters_layout, parent, false);
        return new searchHolder(view);
    }

    @Override
    public void onBindViewHolder(searchHolder holder, int position) {

        searchStudentClass current = (searchStudentClass) arrayList.get(position);
        holder.naem.setText(current.getName());
        holder.rsid.setText(current.getRsid());
        holder.address.setText(current.getAddress());
        holder.fname.setText(current.getFname());
        holder.smob.setText(current.getSmob_no());
        holder.fmob.setText(current.getPmob_no());

        Picasso.with(mContext)
                .load(URLclass.GET_PIC_PATH + current.getPic_path())
                .into(holder.pic);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class searchHolder extends RecyclerView.ViewHolder {
        private TextView naem, rsid, fname, address, smob, fmob;
        private ImageView pic;

        public searchHolder(View itemView) {
            super(itemView);
        }
    }
}
