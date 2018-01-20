package com.example.mayank.rooms.Student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager llm;
    private LinearLayout linearLayout;
    private String city, near, forWhom, floor, nocoac, to, from;
    private boolean guest;
    private ArrayList<searchClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.linearLayout);
        recyclerView = findViewById(R.id.recyclerView);

        llm = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Search Results");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrayList = new ArrayList<>();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        city = bundle.getString("city");
        near = bundle.getString("near");
        forWhom = bundle.getString("near");
        floor = bundle.getString("floor");
        nocoac = bundle.getString("nocoac");
        guest = bundle.getBoolean("guest");
        to = bundle.getString("to");
        from = bundle.getString("from");

        String[] searchParams = new String[]{city, near, forWhom, floor, nocoac, from + " - " + to};

        for (String searchParam : searchParams) {
            addView(searchParam);
        }

        if (guest) {
            addView("Guest");
        }

        getSearchResults();

    }

    private void addView(String string) {
        View tags = LayoutInflater.from(this).inflate(R.layout.tags, linearLayout);
        TextView text = tags.findViewById(R.id.tags);
        ImageView cut = tags.findViewById(R.id.cut);
        cut.setVisibility(View.GONE);
        text.setText(string);
        linearLayout.addView(text);
    }

    private void getSearchResults() {
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray searchRes = jsonObject.getJSONArray("searchDetails");
                            for (int i = 0; i < searchRes.length(); i++) {
                                JSONObject detail = searchRes.getJSONObject(i);
                                String hid = detail.getString("id");
                                String room_id = detail.getString("roomId");
                                String address = detail.getString("address");
                                String mob = detail.getString("mob");
                                String rent = detail.getString("rent");
                                String pic = detail.getString("pic");
                                arrayList.add(new searchClass(hid, room_id, address, mob, rent, pic));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        searchAdapter adapter = new searchAdapter(SearchResultsActivity.this, arrayList);
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
                params.put("city", city);
                params.put("tags", near);
                params.put("gender", forWhom);
                params.put("guest", guest + "");
                params.put("rentTo", to);
                params.put("rentFrom", from);
                params.put("nocoac", nocoac);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
