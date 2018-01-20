package com.example.mayank.rooms.Parent;

import android.content.Context;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParentsMainActivity extends AppCompatActivity {

    private ImageView dp;
    private TextView stu_name;
    private TextView ins_name;
    private RecyclerView recyclerView;
    private String sid;
    private ArrayList<parentClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_main);

        recyclerView = findViewById(R.id.recyclerView);
        dp = findViewById(R.id.dp);
        stu_name = findViewById(R.id.stu_name);
        ins_name = findViewById(R.id.ins_name);

        arrayList = new ArrayList<>();

        sid = getIntent().getStringExtra("sid");

        getDetails();
    }

    private void getDetails() {
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject details = jsonObject.getJSONObject("details");
                    stu_name.setText(details.getString("name"));
                    ins_name.setText(details.getString("ins"));

                    JSONArray jsonArray = jsonObject.getJSONArray("couns");
                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        String date = json.getString("date");
                        String desc = json.getString("desc");
                        String rating = json.getString("rating");
                        arrayList.add(new parentClass(date, desc, rating));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parentsAdapter adapter = new parentsAdapter(ParentsMainActivity.this, arrayList);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sid", sid);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }


    private class parentClass {

        private String date;
        private String message;
        private String rating;

        public parentClass(String date, String message, String rating) {
            this.date = date;
            this.rating = rating;
            this.message = message;
        }

        public String getRating() {
            return rating;
        }

        public String getDate() {
            return date;
        }

        public String getMessage() {
            return message;
        }
    }

    private class parentsAdapter extends RecyclerView.Adapter<parentsAdapter.parentsViewholder>{

        private Context mContext;
        private ArrayList arrayList;

        public parentsAdapter(Context mContext, ArrayList arrayList){
            this.mContext = mContext;
            this.arrayList = arrayList;

        }

        @Override
        public parentsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.parents_layout, parent);
            return new parentsViewholder(view);
        }

        @Override
        public void onBindViewHolder(parentsViewholder holder, int position) {

            parentClass current = (parentClass) arrayList.get(position);
            holder.date.setText(current.getDate());
            holder.message.setText(current.getMessage());
            holder.ratingBar.setRating(Float.parseFloat(current.getRating()));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class parentsViewholder extends RecyclerView.ViewHolder {
            private TextView date, message;
            private RatingBar ratingBar;

            public parentsViewholder(View itemView) {
                super(itemView);
                date = itemView.findViewById(R.id.date);
                message = itemView.findViewById(R.id.desc);
                ratingBar = itemView.findViewById(R.id.ratingBar);
            }
        }

    }
}
