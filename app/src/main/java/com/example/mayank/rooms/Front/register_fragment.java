package com.example.mayank.rooms.Front;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.House_owner.addRoomActivity;
import com.example.mayank.rooms.LoadingClass;
import com.example.mayank.rooms.URLclass;
import com.example.mayank.rooms.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.mayank.rooms.Constants.showError;

/**
 * A simple {@link Fragment} subclass.
 */
public class register_fragment extends Fragment {


    private EditText code;
    private Button proceed;
    private View parent;
    private LoadingClass lc;


    public register_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        code = (EditText) view.findViewById(R.id.entryCode);
        proceed = (Button) view.findViewById(R.id.button);
        parent = view.findViewById(R.id.register_parent);

        lc = new LoadingClass(getActivity());

        proceed.setEnabled(false);
        proceed.setBackgroundColor(Color.parseColor("#ffa4a2"));

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    String entryCode = s.toString();
                    verifyCode(entryCode);
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), register_activity.class));
            }
        });


        return view;

    }

    public void verifyCode(final String code) {
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLclass.verifyCode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String status = res.getString("status");
                            if (status.equals("true")) {
                                proceed.setEnabled(true);
                                proceed.setBackgroundColor(Color.parseColor("#af4448"));
                            } else {
                                Snackbar.make(parent, "Invalid Entry Code", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Error in VC parsing", e + "");
                        }
                        lc.dismissDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lc.dismissDialog();
                        if (error instanceof TimeoutError) {
                            Toast.makeText(getActivity(), "Time out. Reload.", Toast.LENGTH_SHORT).show();
                        } else
                            showError(error, getActivity().getClass().getName(), getActivity());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("code", code); //HAS TO PASS THE CUSTOMER ID AS PARAMETER
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

}
