package com.example.mayank.rooms.Parent;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.example.mayank.rooms.URLclass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView pnumber;
    private TextView cnumber;
    private TextView loginButton;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        pnumber = findViewById(R.id.pNumber);
        cnumber = findViewById(R.id.cnumber);
        loginButton = findViewById(R.id.loginButton);
        view = findViewById(R.id.loginParent);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pnumber.getText().length() == 0 || cnumber.getText().length() == 0) {
                    Snackbar.make(view, "Some Fields Are Left Empty", Snackbar.LENGTH_SHORT).show();
                } else
                    checkLoginServer(pnumber.getText().toString(), cnumber.getText().toString());
            }
        });
    }

    public void checkLoginServer(final String pnumber, final String cnumber) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URLclass.LOGIN_PARENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String status = res.getString("status");
                            if (status.equals("success")) {

                            } else {
                                Snackbar.make(view, "Wrong number, password or user. Please check.", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error in Resonse/Event", error + "");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("pnumber", pnumber); //HAS TO PASS THE CUSTOMER ID AS PARAMETER
                parameters.put("cnumber", cnumber);
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
