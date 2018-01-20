package com.example.mayank.rooms.Student;

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
import com.example.mayank.rooms.Constants;
import com.example.mayank.rooms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MotivationalActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private String stuId;
    private ArrayList<MotivationalClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivational);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);

        llm = new LinearLayoutManager(this);
        arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        stuId = getIntent().getStringExtra(Constants.STUDENT_ID);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Motivational");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getMotivationalData();

    }

    private void getMotivationalData(){
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("details");
                    for(int i = 0;i<details.length();i++){
                        JSONObject md = details.getJSONObject(i);
                        String path = md.getString("path");
                        String story = md.getString("story");
                        arrayList.add(new MotivationalClass(path, story));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MotivationalAdapter adapter = new MotivationalAdapter(MotivationalActivity.this, arrayList);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("stu_id", stuId);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public class MotivationalClass{

        private String path;
        private String story;

        public MotivationalClass(String path, String story){
            this.path = path;
            this.story = story;
        }

        public String getPath() {
            return path;
        }

        public String getStory() {
            return story;
        }
    }
}
