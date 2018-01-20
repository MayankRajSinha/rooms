package com.example.mayank.rooms.Counsellor;

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.LoadingClass;
import com.example.mayank.rooms.R;
import com.example.mayank.rooms.URLclass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private Button login;
    private Toolbar toolbar;
    private View parent;
    private LoadingClass lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login4);

        parent = findViewById(R.id.loginParent);
        id = findViewById(R.id.cid);
        password = findViewById(R.id.loginPassword);
        login = findViewById(R.id.loginButton);
        toolbar = findViewById(R.id.toolbar);

        lc = new LoadingClass(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Counsellor");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().length() == 0 || password.getText().length() == 0) {
                    Snackbar.make(parent, "Some Fields Are Left Empty", Snackbar.LENGTH_SHORT).show();
                } else
                    checkLogin();
            }
        });
    }

    public void checkLogin() {
        lc.showDialog();
        String getNumber = id.getText().toString();
        String getPassword = password.getText().toString();
        checkLoginServer(getNumber, getPassword);
    }

    public void checkLoginServer(final String number, final String password) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URLclass.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String status = res.getString("status");
                            if (status.equals("success")) {

                            } else {
                                Snackbar.make(parent, "Wrong counseller Id or password. Please check.", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        lc.dismissDialog();
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
                parameters.put("number", number); //HAS TO PASS THE CUSTOMER ID AS PARAMETER
                parameters.put("password", password);
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
