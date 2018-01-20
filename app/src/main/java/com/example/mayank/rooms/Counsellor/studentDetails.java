package com.example.mayank.rooms.Counsellor;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class studentDetails extends AppCompatActivity {

    private ImageView imageView;
    private TextView name;
    private TextView fname;
    private TextView laddress;
    private TextView paddress;
    private FloatingActionButton review;
    private String dpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        imageView = findViewById(R.id.dp);
        name = findViewById(R.id.name);
        fname = findViewById(R.id.fname);
        laddress = findViewById(R.id.laddress);
        paddress = findViewById(R.id.paddress);
        review = findViewById(R.id.review);

        String sid = getIntent().getStringExtra("sid");

        getDetails(sid);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void getDetails(final String sid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            name.setText(jsonObject.getString("name"));
                            fname.setText(jsonObject.getString("fname"));
                            laddress.setText(jsonObject.getString("laddress"));
                            paddress.setText(jsonObject.getString("paddress"));

                            dpUrl = jsonObject.getString("dpUrl");

                            Picasso.with(studentDetails.this).load("").into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sid", sid);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
