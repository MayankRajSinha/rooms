package com.example.mayank.rooms.Student;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class AchievementsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<achievementsClass> arrayList;
    private TextView add;
    private String sid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        add = findViewById(R.id.add);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Institute Message");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        arrayList = new ArrayList<>();

        sid = getIntent().getStringExtra("sid");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        getAchievements();
    }

    private class achievementsClass {

        private String date;
        private String message;

        public achievementsClass(String date, String message) {
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


    private void getAchievements() {
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
                        arrayList.add(new achievementsClass(date, message));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                achievementsAdapter adapter = new achievementsAdapter(AchievementsActivity.this, arrayList);
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


    private class achievementsAdapter extends RecyclerView.Adapter<achievementsAdapter.achievementsViewHolder> {

        private Context mContext;
        private ArrayList arrayList;

        achievementsAdapter(Context mContext, ArrayList arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;

        }

        @Override
        public achievementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.institute_message_view_layout, parent);
            return new achievementsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(achievementsViewHolder holder, int position) {
            achievementsClass current = (achievementsClass) arrayList.get(position);

            holder.date.setText(current.getDate());
            holder.message.setText(current.getMessage());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }


        class achievementsViewHolder extends RecyclerView.ViewHolder {
            private TextView date, message;

            achievementsViewHolder(View itemView) {
                super(itemView);

                date = itemView.findViewById(R.id.date);
                message = itemView.findViewById(R.id.message);
            }
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_achievement_dialog);

        final boolean[] b = {false};
        final TextView date = dialog.findViewById(R.id.addDate);
        final EditText achie = dialog.findViewById(R.id.achievements);
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
                    addAchievementInServer(d, getAchi);
                } else
                    Toast.makeText(AchievementsActivity.this, "Pick a date.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAchievementInServer(final String date, final String a) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getAchievements();
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
                params.put("date", date);
                params.put("ach", a);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
