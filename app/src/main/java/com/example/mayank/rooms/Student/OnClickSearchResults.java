package com.example.mayank.rooms.Student;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class OnClickSearchResults extends AppCompatActivity {


    private String roomId;
    private TextView rent;
    private TextView address;
    private TextView near;
    private TextView pincode;
    private TextView coolerRate;
    private TextView acRate;
    private CheckBox cooler;
    private CheckBox ac;
    private TextView map;
    private TextView book;
    private TextView desc;
    private LinearLayout acLayout, coolerLayout;
    private String nocoac, directions, rate;
    private String[] img = new String[3];
    private String noi;
    private onPicUpdate picUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_search_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        final ViewPager pager = findViewById(R.id.pager);
        rent = findViewById(R.id.rent);
        address = findViewById(R.id.address);
        near = findViewById(R.id.near);
        pincode = findViewById(R.id.pincode);
        cooler = findViewById(R.id.coolerCB);
        ac = findViewById(R.id.acCB);
        coolerRate = findViewById(R.id.coolerRate);
        acRate = findViewById(R.id.acRate);
        map = findViewById(R.id.getDirections);
        book = findViewById(R.id.book);
        acLayout = findViewById(R.id.acLayout);
        coolerLayout = findViewById(R.id.coolerLayout);
        desc = findViewById(R.id.desc);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Motivational Story");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordinates[] = directions.split(",");
                Uri gmmIntentUri = Uri.parse("geo:" + coordinates[0] + "," + coordinates[1]);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }

            }
        });

        roomId = getIntent().getStringExtra("roomId");
        near.setText(getIntent().getStringExtra("near"));

        getDetails();

        picUpdate = new onPicUpdate() {
            @Override
            public void update() {
                pager.setOffscreenPageLimit(3);
                OnClickMotivationalActivity.CustomPagerAdapter adapter = new OnClickMotivationalActivity.CustomPagerAdapter(OnClickSearchResults.this, noi, img);
                pager.setAdapter(adapter);
            }
        };

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    rent.setText('\u20B9' + jsonObject.getString("rent"));
                    address.setText(jsonObject.getString("address"));
                    pincode.setText(jsonObject.getString("pincode"));
                    desc.setText(jsonObject.getString("desc"));
                    img[0] = jsonObject.getString("pic1");
                    img[1] = jsonObject.getString("pic2");
                    img[2] = jsonObject.getString("pic3");
                    noi = jsonObject.getString("noi");

                    nocoac = jsonObject.getString("nocoac");
                    directions = jsonObject.getString("directions");
                    rate = jsonObject.getString("rate");

                    if (nocoac.equals("Ac")) {
                        ac.setChecked(true);
                        acRate.setText('\u20B9' + rate);
                        coolerLayout.setVisibility(View.GONE);
                    } else {
                        cooler.setChecked(true);
                        coolerRate.setText('\u20B9' + rate);
                        acLayout.setVisibility(View.GONE);
                    }

                    picUpdate.update();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("roomId", roomId);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private interface onPicUpdate {
        void update();
    }
}

