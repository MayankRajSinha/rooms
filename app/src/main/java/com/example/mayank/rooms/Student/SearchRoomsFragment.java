package com.example.mayank.rooms.Student;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRoomsFragment extends Fragment {

    private String sid;
    private Spinner city;
    private Spinner forWhom;
    private Spinner floor;
    private Spinner nocoac;
    private AutoCompleteTextView nearPlaces;
    private TextView search;
    private CheckBox guest;
    private EditText to, from;

    public SearchRoomsFragment() {
        // Required empty public constructor
    }

    public static SearchRoomsFragment newInstance(String sid) {
        SearchRoomsFragment fragment = new SearchRoomsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.STUDENT_ID, sid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sid = getArguments().getString(Constants.STUDENT_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        city = view.findViewById(R.id.select_city);
        forWhom = view.findViewById(R.id.forWhom);
        nearPlaces = view.findViewById(R.id.nearPlaces);
        floor = view.findViewById(R.id.floor);
        search = view.findViewById(R.id.search);
        nocoac = view.findViewById(R.id.nocoac);
        guest = view.findViewById(R.id.guests);
        to = view.findViewById(R.id.to);
        from = view.findViewById(R.id.from);

        final String selectedCity = city.getSelectedItem().toString();
        final String selectedGender = forWhom.getSelectedItem().toString();
        final String near = nearPlaces.getText().toString();
        final String selectedFloor = floor.getSelectedItem().toString();
        final String selectedNocoac = nocoac.getSelectedItem().toString();
        final boolean isGuest = guest.isChecked();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int TO = Integer.parseInt(to.getText().toString());
                int FROM = Integer.parseInt(from.getText().toString());
                if (to.getText().length() == 0 || from.getText().length() == 0 || nearPlaces.getText().length() == 0) {
                    Toast.makeText(getActivity(), "All fields are compulsory.", Toast.LENGTH_SHORT).show();
                } else if (FROM > TO) {
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("city", selectedCity);
                    bundle.putString("forWhom", selectedGender);
                    bundle.putString("near", near);
                    bundle.putString("floor", selectedFloor);
                    bundle.putString("nocoac", selectedNocoac);
                    bundle.putString("to", to.getText().toString());
                    bundle.putString("from", from.getText().toString());
                    bundle.putBoolean("guest", isGuest);
                    Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

}
