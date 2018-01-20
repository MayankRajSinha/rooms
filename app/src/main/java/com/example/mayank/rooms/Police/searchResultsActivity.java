package com.example.mayank.rooms.Police;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.LoadingClass;
import com.example.mayank.rooms.R;
import com.example.mayank.rooms.searchAdapter;
import com.example.mayank.rooms.searchStudentClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchResultsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LoadingClass lc;
    private ArrayList<searchStudentClass> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results2);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        arrayList = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        lc = new LoadingClass(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Search Results");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().getExtras() != null){
            String searchBy = getIntent().getStringExtra("searchBy");
            String tag = getIntent().getStringExtra("tag");
            searchStressedStudent(searchBy, tag);
        }
    }

    private void searchStressedStudent(final String searchBy, final String searchTag){
        lc.showDialog();
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for(int i = 0;i< jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name = jsonObject1.getString("name");
                        String rsid = jsonObject1.getString("rsid");
                        String fname = jsonObject1.getString("fname");
                        String address = jsonObject1.getString("address");
                        String smob = jsonObject1.getString("smob");
                        String fmob = jsonObject1.getString("fmob");
                        String pic_path = jsonObject1.getString("pic_path");
                        arrayList.add(new searchStudentClass(pic_path, name, rsid, fname, address, smob, fmob));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                searchAdapter adapter = new searchAdapter(searchResultsActivity.this, arrayList);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("searchBy", searchBy);
                params.put("tags", searchTag);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
