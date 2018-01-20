package com.example.mayank.rooms.Front;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.LoadingClass;
import com.example.mayank.rooms.URLclass;
import com.example.mayank.rooms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class event_fragment extends Fragment {
    private RecyclerView events_recylerView;
    private RecyclerView.LayoutManager llm;
    private ArrayList<eventContainer> eventArraylist;
    private LoadingClass lc;

    public event_fragment() {
        // Required empty public constructor
    }

    public static event_fragment newInstance() {
        return new event_fragment();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.event_fragment, container, false);

        events_recylerView = (RecyclerView) view.findViewById(R.id.events_recyclerView);

        eventArraylist = new ArrayList<>();
        events_recylerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        events_recylerView.setLayoutManager(llm);

        lc = new LoadingClass(getActivity());

        getEvents();


        return view;
    }

    /**
     * This function gets all the event related details from database
     */
    public void getEvents() {
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLclass.getEventDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            eventArraylist.clear();
                            JSONObject main = new JSONObject(response);
                            JSONArray events = main.getJSONArray("events");
                            for (int i = 0; i < events.length(); i++) {
                                JSONObject details = events.getJSONObject(i);
                                String title = details.getString("title");
                                String description = details.getString("description");
                                eventArraylist.add(new eventContainer(title, description));
                            }
                            EventsAdapter adapter = new EventsAdapter(eventArraylist, getActivity());
                            events_recylerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            eventArraylist.clear();
                            Log.e("Error in event", e + "");
                        }

                        lc.dismissDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error in Resonse/Event", error + "");
                    }
                });

        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


}
