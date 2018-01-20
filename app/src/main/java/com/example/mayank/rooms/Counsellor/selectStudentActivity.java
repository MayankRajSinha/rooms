package com.example.mayank.rooms.Counsellor;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class selectStudentActivity extends AppCompatActivity {

    private EditText sid;
    private Button ok;
    private View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        sid = findViewById(R.id.sid);
        ok = findViewById(R.id.ok);
        parent = findViewById(R.id.loginParent);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSid(sid.getText().toString());
            }
        });
    }
    private void checkSid(final String sid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if(status.equals("true")){
                                Intent intent = new Intent(selectStudentActivity.this, studentDetails.class);
                                intent.putExtra("sid", sid);
                                startActivity(intent);
                            }else {
                                Snackbar.make(parent, "Student Id is incorrect", Snackbar.LENGTH_SHORT).show();
                            }
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
                params.put("sid", sid);
                return params;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
