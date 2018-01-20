package com.example.mayank.rooms.Student;

import android.Manifest;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.House_owner.addRoomActivity;
import com.example.mayank.rooms.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class stu_details extends AppCompatActivity {

    private ImageView dp, ins_id_img, id_img;
    private EditText name;
    private EditText f_name;
    private Spinner ins_name;
    private EditText ins_number;
    private EditText id_name;
    private EditText id_num;
    private EditText address;
    private EditText city;
    private EditText state;
    private EditText s_number;
    private EditText f_number;
    private EditText street;
    private EditText pincode;
    private TextView update;
    private final int REQUEST_CAMERA = 100;
    private int select = 0;
    private String imgPath1, imgPath2, imgPath3;
    private boolean photoChanged = false;
    private int count = 0;
    private ArrayList<String> insList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_details);

        dp = findViewById(R.id.dp);
        ins_id_img = findViewById(R.id.institute_id_img);
        id_img = findViewById(R.id.aadhar_img);
        name = findViewById(R.id.stu_name);
        f_name = findViewById(R.id.f_name);
        ins_name = findViewById(R.id.ins_name);
        ins_number = findViewById(R.id.institute_id);
        id_name = findViewById(R.id.id_card);
        id_num = findViewById(R.id.id_number);
        address = findViewById(R.id.address);
        city = findViewById(R.id.owner_city);
        state = findViewById(R.id.owner_state);
        s_number = findViewById(R.id.stu_number);
        f_number = findViewById(R.id.fnumber);
        street = findViewById(R.id.owner_street);
        pincode = findViewById(R.id.owner_pin);
        update = findViewById(R.id.save);

        disable();
        insList = new ArrayList<>();

        getInstitute();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update.getText().toString().equals("Edit")) {
                    enable();
                    update.setText(R.string.update);
                } else {
                    disable();
                    update.setText(R.string.edit);
                  /*  if (photoChanged)
                        uploadImage();
                    else
                        updateData();*/
                }
            }
        });


        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 1;
            }
        });

        ins_id_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 2;
            }
        });

        id_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntentForImage();
                select = 3;
            }
        });

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
                                imgPath1 = file.getPath();
                                dp.setScaleType(ImageView.ScaleType.FIT_XY);
                                dp.setImageBitmap(bitmap);
                                break;

                            case 2:
                                imgPath2 = file.getPath();
                                ins_id_img.setScaleType(ImageView.ScaleType.FIT_XY);
                                ins_id_img.setImageBitmap(bitmap);
                                break;

                            case 3:
                                imgPath3 = file.getPath();
                                id_img.setScaleType(ImageView.ScaleType.FIT_XY);
                                id_img.setImageBitmap(bitmap);
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

    private void getInstitute(){

        insList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ins = new JSONArray(response);
                            for(int i =0;i<ins.length();i++){
                                JSONObject inst = ins.getJSONObject(i);
                                insList.add(inst.getString("inst"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter(stu_details.this, android.R.layout.simple_spinner_item, insList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        ins_name.setAdapter(adapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    private void sendIntentForImage(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(stu_details.this);
            builder.setTitle("Permission to read and write to storage !!");
            builder.setMessage("This app needs permission to read and write images to storage.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ActivityCompat.requestPermissions(stu_details.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
                .getIntent(stu_details.this);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void uploadImages(String imgPath){

        try {
            long time = System.currentTimeMillis();
            final String path = "_" + time + imgPath.substring(imgPath.lastIndexOf("."));
            new MultipartUploadRequest(stu_details.this, "")
                    .addFileToUpload(imgPath, "image")
                    .addParameter("name", path)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(10)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Toast.makeText(stu_details.this, "Error Occured. Please Retry.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload();
        } catch (Exception e) {

        }

    }

    private void disable() {

        dp.setEnabled(false);
        ins_id_img.setEnabled(false);
        id_img.setEnabled(false);
        name.setEnabled(false);
        f_name.setEnabled(false);
        ins_name.setEnabled(false);
        ins_number.setEnabled(false);
        id_name.setEnabled(false);
        id_num.setEnabled(false);
        address.setEnabled(false);
        city.setEnabled(false);
        state.setEnabled(false);
        s_number.setEnabled(false);
        f_number.setEnabled(false);
        street.setEnabled(false);
        pincode.setEnabled(false);

    }

    private void enable() {

        dp.setEnabled(true);
        ins_id_img.setEnabled(true);
        id_img.setEnabled(true);
        name.setEnabled(true);
        f_name.setEnabled(true);
        ins_name.setEnabled(true);
        ins_number.setEnabled(true);
        id_name.setEnabled(true);
        id_num.setEnabled(true);
        address.setEnabled(true);
        city.setEnabled(true);
        state.setEnabled(true);
        s_number.setEnabled(true);
        f_number.setEnabled(true);
        street.setEnabled(true);
        pincode.setEnabled(true);

    }
}
