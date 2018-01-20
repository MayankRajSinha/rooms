package com.example.mayank.rooms.House_owner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class renterFragment extends Fragment {

    private String houseId;
    private RecyclerView renterRecyclerView;
    private LinearLayoutManager llm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<renterClass> arrayList;


    public renterFragment() {
        // Required empty public constructor
    }

    public static renterFragment newInstance(String hid) {
        renterFragment fragment = new renterFragment();
        Bundle args = new Bundle();
        args.putString(Constants.HOUSE_OWNER_ID, hid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            houseId = getArguments().getString(Constants.HOUSE_OWNER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_renter, container, false);
        renterRecyclerView = view.findViewById(R.id.renterRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        arrayList = new ArrayList<>();
        llm = new LinearLayoutManager(getActivity());
        renterRecyclerView.setLayoutManager(llm);
        renterRecyclerView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRenters();
            }
        });


        getRenters();

        return view;
    }

    private void getRenters(){
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arrayList.clear();
                            JSONObject json = new JSONObject(response);
                            JSONArray statusArray = json.getJSONArray("");
                            for (int i =0;i<statusArray.length();i++){
                                JSONObject statusObject = statusArray.getJSONObject(i);
                                String roomId = statusObject.getString("");
                                String name = statusObject.getString("");
                                String fname = statusObject.getString("");
                                String sno = statusObject.getString("");
                                String fno = statusObject.getString("");
                                String pic_path = statusObject.getString("");
                                String address = statusObject.getString("");
                                arrayList.add(new renterClass(pic_path, name, roomId, fname, address, sno, fno));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(true);
                        }

                        renterAdapter adapter = new renterAdapter(getActivity(), arrayList);
                        renterRecyclerView.setAdapter(adapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(true);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("hid", houseId);
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
