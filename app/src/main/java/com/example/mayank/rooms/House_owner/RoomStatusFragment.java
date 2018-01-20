package com.example.mayank.rooms.House_owner;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.Constants;
import com.example.mayank.rooms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RoomStatusFragment extends Fragment {


    private String hid;
    private RecyclerView roomStatusRv;
    private LinearLayoutManager llm;
    private ArrayList<roomStatusClass> arrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView addRoom;


    public RoomStatusFragment() {

    }

    public static RoomStatusFragment newInstance(String id) {
        RoomStatusFragment fragment = new RoomStatusFragment();
        Bundle args = new Bundle();
        args.putString(Constants.HOUSE_OWNER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hid = getArguments().getString(Constants.HOUSE_OWNER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_status, container, false);
        roomStatusRv = view.findViewById(R.id.roomStatusRv);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        addRoom = view.findViewById(R.id.addRoom);

        llm = new LinearLayoutManager(getActivity());
        roomStatusRv.setLayoutManager(llm);
        roomStatusRv.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        getRoomStatus();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRoomStatus();
            }
        });

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void getRoomStatus(){
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
                      String forGender = statusObject.getString("");
                      String rent = statusObject.getString("");
                      String accomodated = statusObject.getString("");
                      String guest = statusObject.getString("");
                      String pic_path = statusObject.getString("");
                      arrayList.add(new roomStatusClass(roomId, forGender, rent, accomodated, guest, pic_path));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(true);
                }

                roomStatusAdapter adapter = new roomStatusAdapter(getActivity(), arrayList);
                roomStatusRv.setAdapter(adapter);

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
                parameters.put("hid", hid);
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


}
