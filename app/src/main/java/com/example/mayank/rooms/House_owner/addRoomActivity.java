package com.example.mayank.rooms.House_owner;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.mayank.rooms.Constants.showError;

public class addRoomActivity extends AppCompatActivity {


    private Spinner forWhom;
    private Spinner floor;
    private CheckBox coolerCB;
    private CheckBox acCB;
    private CheckBox guest;
    private EditText description;
    private EditText cooler;
    private EditText ac;
    private EditText rent;
    private LinearLayout coolerLayout;
    private LinearLayout acLayout;
    private ImageView main, extra1, extra2, mainex, extraex1, extraex2;
    private TextView update;
    private TextView rules;
    private final int REQUEST_CAMERA = 100;
    private int select = 0;
    private String imgPath1, imgPath2, imgPath3;
    private boolean photoChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);


        forWhom = findViewById(R.id.forWhom);
        floor = findViewById(R.id.floor);
        coolerCB = findViewById(R.id.coolerCB);
        acCB = findViewById(R.id.acCB);
        guest = findViewById(R.id.guest);
        description = findViewById(R.id.description);
        cooler = findViewById(R.id.cooler);
        ac = findViewById(R.id.ac);
        rent = findViewById(R.id.rent);
        coolerLayout = findViewById(R.id.coolerLayout);
        acLayout = findViewById(R.id.acLayout);
        main = findViewById(R.id.mainView);
        extra1 = findViewById(R.id.extra1);
        extra2 = findViewById(R.id.extra2);
        mainex = findViewById(R.id.mainpic);
        extraex1 = findViewById(R.id.extraex1);
        extraex2 = findViewById(R.id.extraex2);
        rules = findViewById(R.id.rules);
        update = findViewById(R.id.save);



        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 1;
            }
        });

        extra1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 2;
            }
        });

        extra2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 3;
            }
        });

        coolerCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    coolerLayout.setVisibility(View.VISIBLE);
                else
                    coolerLayout.setVisibility(View.GONE);
            }
        });

        acCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    acLayout.setVisibility(View.VISIBLE);
                else
                    acLayout.setVisibility(View.GONE);
            }
        });

        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
            }
        });

    }


    private void sendIntentForImage(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(addRoomActivity.this);
            builder.setTitle("Permission to read and write to storage !!");
            builder.setMessage("This app needs permission to read and write images to storage.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ActivityCompat.requestPermissions(addRoomActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            2);
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            openImageIntent();
        }
    }

    private void openImageIntent() {
        Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(120, 120)
                .getIntent(addRoomActivity.this);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        String filename = System.currentTimeMillis() + ".jpg";
                        File file = new File(getFilesDir(), filename);

                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);

                        switch (select){

                            case 1:
                                mainex.setVisibility(View.GONE);
                                imgPath1 = file.getPath();
                                main.setScaleType(ImageView.ScaleType.FIT_XY);
                                main.setImageBitmap(bitmap);
                                break;

                            case 2:
                                extraex1.setVisibility(View.GONE);
                                imgPath2 = file.getPath();
                                extra1.setScaleType(ImageView.ScaleType.FIT_XY);
                                extra1.setImageBitmap(bitmap);
                                break;

                            case 3:
                                extraex2.setVisibility(View.GONE);
                                imgPath3 = file.getPath();
                                extra2.setScaleType(ImageView.ScaleType.FIT_XY);
                                extra2.setImageBitmap(bitmap);
                                break;

                        }
                        photoChanged = true;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Some Error Occured. Please Try Again.", Toast.LENGTH_SHORT).show();
                }


        }
    }

    private void addRoom(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("success"))
                        Toast.makeText(addRoomActivity.this, "Room Added Successfully.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(addRoomActivity.this, "Error Occured. Please Try Again.", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(addRoomActivity.this, "Time out. Reload.", Toast.LENGTH_SHORT).show();
                } else
                    showError(error, addRoomActivity.this.getClass().getName(), addRoomActivity.this);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap();
                parameters.put("forWhom", forWhom.getSelectedItem().toString());
                parameters.put("floor", floor.getSelectedItem().toString());
                parameters.put("rent", rent.getText().toString());
                parameters.put("cooler", coolerCB.isChecked()+"");
                parameters.put("coolerRate", cooler.getText().toString());
                parameters.put("ac", acCB.isChecked()+"");
                parameters.put("acRate", ac.getText().toString());
                parameters.put("guest", guest.isChecked()+"");
                parameters.put("des", description.getText().toString());
                return parameters;
            }
        };

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



}
