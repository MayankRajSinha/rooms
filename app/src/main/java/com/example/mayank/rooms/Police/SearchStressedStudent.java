package com.example.mayank.rooms.Police;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.LoadingClass;
import com.example.mayank.rooms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchStressedStudent extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinner;
    private AutoCompleteTextView enterThat;
    private Button search;
    private View parent;
    private ArrayList<String> studentNameArrayList;
    private ArrayList<String> studentIdArrayList;
    private ArrayList<String> parentNameArrayList;
    private ArrayList<String> townrrayList;
    private ArrayList<String> instituteArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private LoadingClass lc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_stressed_student);

        toolbar = findViewById(R.id.toolbar);
        spinner = findViewById(R.id.spinner);
        enterThat = findViewById(R.id.enterThat);
        search = findViewById(R.id.search);
        parent = findViewById(R.id.parent);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Search Stressed Student");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lc = new LoadingClass(this);

        studentIdArrayList = new ArrayList<>();
        studentNameArrayList = new ArrayList<>();
        parentNameArrayList = new ArrayList<>();
        townrrayList = new ArrayList<>();
        instituteArrayList = new ArrayList<>();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItem().toString()){
                    case "Name":
                        getStudentName();
                        break;

                    case "Id":
                        getStudentById();
                        break;

                    case "Parent Name":
                        getParentName();
                        break;

                    case "City":
                        getTown();
                        break;

                    case "Institute":
                        getInstitute();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enterThat.length() == 0){
                    Snackbar.make(parent, "Some fields are left empty", Snackbar.LENGTH_SHORT).show();
                }else {
                   Intent intent = new Intent(SearchStressedStudent.this, searchResultsActivity.class);
                   intent.putExtra("searchBy", spinner.getSelectedItem().toString());
                   intent.putExtra("tag", enterThat.getText().toString());
                   startActivity(intent);
                }
            }
        });

    }



    private void getStudentName(){
        enterThat.setAdapter(null);
        studentNameArrayList.clear();
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("");
                    for(int i = 0;i<details.length();i++){
                        String name = details.getString(i);
                        studentNameArrayList.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter = new ArrayAdapter<String>(SearchStressedStudent.this, android.R.layout.simple_dropdown_item_1line,studentNameArrayList);
                enterThat.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



    private void getStudentById(){
        studentIdArrayList.clear();
        enterThat.setAdapter(null);
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("");
                    for(int i = 0;i<details.length();i++){
                        String name = details.getString(i);
                        studentIdArrayList.add(name);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrayAdapter = new ArrayAdapter<String>(SearchStressedStudent.this, android.R.layout.simple_dropdown_item_1line,studentIdArrayList);
                enterThat.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getParentName(){
        parentNameArrayList.clear();
        enterThat.setAdapter(null);
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("");
                    for(int i = 0;i<details.length();i++){
                        String name = details.getString(i);
                        parentNameArrayList.add(name);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter = new ArrayAdapter<String>(SearchStressedStudent.this, android.R.layout.simple_dropdown_item_1line,parentNameArrayList);
                enterThat.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getTown(){
        townrrayList.clear();
        enterThat.setAdapter(null);
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("");
                    for(int i = 0;i<details.length();i++){
                        String name = details.getString(i);
                        townrrayList.add(name);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                arrayAdapter = new ArrayAdapter<String>(SearchStressedStudent.this, android.R.layout.simple_dropdown_item_1line,townrrayList);
                enterThat.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void getInstitute(){
        instituteArrayList.clear();
        enterThat.setAdapter(null);
        lc.showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray details = jsonObject.getJSONArray("");
                    for(int i = 0;i<details.length();i++){
                        String name = details.getString(i);
                        instituteArrayList.add(name);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arrayAdapter = new ArrayAdapter<String>(SearchStressedStudent.this, android.R.layout.simple_dropdown_item_1line,instituteArrayList);
                enterThat.setAdapter(arrayAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lc.dismissDialog();
            }
        });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


}
