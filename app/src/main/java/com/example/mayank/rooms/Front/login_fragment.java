package com.example.mayank.rooms.Front;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mayank.rooms.URLclass;
import com.example.mayank.rooms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class login_fragment extends Fragment {

    private EditText number;
    private EditText password;
    private Spinner loginAS;
    private Button login;
    private View parent;


    public login_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_fragment, container, false);
        parent = view.findViewById(R.id.loginParent);
        number = (EditText) view.findViewById(R.id.loginNumber);
        password = (EditText) view.findViewById(R.id.loginPassword);
        loginAS = (Spinner) view.findViewById(R.id.institute);
        login = (Button) view.findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() == 0 || password.getText().length() == 0) {
                    Snackbar.make(parent, "Some Fields Are Left Empty", Snackbar.LENGTH_SHORT).show();
                } else
                    checkLogin();
            }
        });
        return view;
    }

    public void checkLogin() {
        String getNumber = number.getText().toString();
        String getPassword = password.getText().toString();
        String getUser = loginAS.getSelectedItem().toString();
        checkLoginServer(getNumber, getPassword, getUser);
    }

    public void checkLoginServer(final String number, final String password, final String user) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URLclass.login,
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
                parameters.put("number", number); //HAS TO PASS THE CUSTOMER ID AS PARAMETER
                parameters.put("password", password);
                parameters.put("user", user);
                return parameters;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

}
