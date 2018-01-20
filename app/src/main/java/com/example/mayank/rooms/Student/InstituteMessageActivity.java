package com.example.mayank.rooms.Student;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

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

public class InstituteMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<instituteClass> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_message);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Institute Message");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        arrayList = new ArrayList<>();

        String sid = getIntent().getStringExtra("sid");
        getInstituteMessage(sid);

    }

    private void getInstituteMessage(final String sid) {
        arrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray messages = new JSONArray(response);
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject mess = messages.getJSONObject(i);
                        String date = mess.getString("date");
                        String message = mess.getString("message");
                        arrayList.add(new instituteClass(date, message));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                instituteAdapter adapter = new instituteAdapter(InstituteMessageActivity.this, arrayList);
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

    private class instituteClass {

        private String date;
        private String message;

        public instituteClass(String date, String message) {
            this.date = date;
            this.message = message;
        }

        public String getDate() {
            return date;
        }

        public String getMessage() {
            return message;
        }
    }

    private class instituteAdapter extends RecyclerView.Adapter<instituteAdapter.instituteHolder> {

        private Context mContext;
        private ArrayList arrayList;

        instituteAdapter(Context mContext, ArrayList arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;

        }

        @Override
        public instituteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.institute_message_view_layout, parent);
            return new instituteHolder(view);
        }

        @Override
        public void onBindViewHolder(instituteHolder holder, int position) {
            instituteClass current = (instituteClass) arrayList.get(position);

            holder.date.setText(current.getDate());
            holder.message.setText(current.getMessage());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        class instituteHolder extends RecyclerView.ViewHolder {
            private TextView date, message;

            instituteHolder(View itemView) {
                super(itemView);

                date = itemView.findViewById(R.id.date);
                message = itemView.findViewById(R.id.message);
            }
        }
    }
}
