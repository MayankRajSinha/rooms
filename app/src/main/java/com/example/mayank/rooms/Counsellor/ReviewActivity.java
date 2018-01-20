package com.example.mayank.rooms.Counsellor;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.Parent.ParentsMainActivity;
import com.example.mayank.rooms.R;
import com.example.mayank.rooms.Student.AchievementsActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    private ImageView dp;
    private TextView sname;
    private TextView sid;
    private RecyclerView recyclerView;
    private ArrayList<parentClass> arrayList;
    private String SID;
    private TextView newReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        recyclerView = findViewById(R.id.recyclerView);
        dp = findViewById(R.id.dp);
        sname = findViewById(R.id.name);
        sid = findViewById(R.id.sid);
        newReview = findViewById(R.id.add);

        arrayList = new ArrayList<>();

        sname.setText(getIntent().getStringExtra("sname"));
        sid.setText(getIntent().getStringExtra("sid"));

        Picasso.with(this).load(""+getIntent().getStringExtra("url")).into(dp);

        SID = getIntent().getStringExtra("sid");

        newReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        getDetails();
    }


    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_review_layout);

        final boolean[] b = {false};
        final TextView date = dialog.findViewById(R.id.addDate);
        final EditText achie = dialog.findViewById(R.id.achievements);
        final RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView add = dialog.findViewById(R.id.addButton);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        dialog.dismiss();
                        b[0] = true;
                    }
                };
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b[0]) {
                    String getAchi = achie.getText().toString();
                    String d = date.getText().toString();
                    String rating = ratingBar.getRating()+"";
                    addNewRating(d, getAchi, rating);
                } else
                    Toast.makeText(ReviewActivity.this, "Pick a date.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewRating(final String date, final String a, final String rating) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getDetails();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sid", SID);
                params.put("date", date);
                params.put("ach", a);
                params.put("rating", rating);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void getDetails() {
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("couns");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String date = json.getString("date");
                        String desc = json.getString("desc");
                        String rating = json.getString("rating");
                        arrayList.add(new parentClass(date, desc, rating));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                parentsAdapter adapter = new parentsAdapter(ReviewActivity.this, arrayList);
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
                params.put("sid", SID);
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


    private class parentsAdapter extends RecyclerView.Adapter<parentsAdapter.parentsViewholder> {

        private Context mContext;
        private ArrayList arrayList;

        public parentsAdapter(Context mContext, ArrayList arrayList) {
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
