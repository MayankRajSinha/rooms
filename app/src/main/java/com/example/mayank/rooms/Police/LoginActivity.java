package com.example.mayank.rooms.Police;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.example.mayank.rooms.URLclass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Spinner city;
    private Spinner policeStation;
    private EditText password;
    private Button login;
    private View parent;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login3);

        city = findViewById(R.id.institute);
        policeStation = findViewById(R.id.ps);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        parent = findViewById(R.id.loginParent);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Police");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().length() == 0) {
                    Snackbar.make(parent, "Please Enter Password", Snackbar.LENGTH_SHORT).show();
                } else
                    checkLoginServer(city.getSelectedItem().toString(), policeStation.getSelectedItem().toString(), password.getText().toString());
            }
        });
    }

    public void checkLoginServer(final String city, final String ps, final String password) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URLclass.LOGIN_PARENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String status = res.getString("status");
                            if (status.equals("success")) {

                            } else {
                                Snackbar.make(parent, "Wrong number, password or user. Please check.", Snackbar.LENGTH_SHORT).show();
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
                parameters.put("city", city); //HAS TO PASS THE CUSTOMER ID AS PARAMETER
                parameters.put("ps", ps);
                parameters.put("pass", password);
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
