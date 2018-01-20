package com.example.mayank.rooms.House_owner;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class owner_details extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView owner_name;
    private TextView owner_fathers_name;
    private TextView owner_house_number;
    private TextView owner_house_address;
    private TextView owner_pincode;
    private TextView owner_street;
    private Spinner owner_city;
    private TextView owner_state;
    private TextView owner_update;
    private CheckBox owner_guest;
    private AutoCompleteTextView placesNear;
    private TextView markOnMap;
    private ImageView dp;
    private LinearLayout nearPlacesTag;
    private ArrayList<String> tagsList;
    private LocationRequest locationRequest;
    private final int REQUEST_CHECK_SETTINGS = 100;
    private final int REQUEST_CAMERA = 200;
    private LocationCallback mLocationCallback;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private ImageView check;
    private String imgPath;
    private boolean photoChanged = false;


    public owner_details() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owner_details, container, false);

        owner_name = view.findViewById(R.id.owner_name);
        owner_fathers_name = view.findViewById(R.id.owner_pname);
        owner_house_number = view.findViewById(R.id.owner_hname);
        owner_house_address = view.findViewById(R.id.owner_add);
        owner_pincode = view.findViewById(R.id.owner_pin);
        owner_street = view.findViewById(R.id.owner_street);
        owner_city = view.findViewById(R.id.owner_city);
        owner_state = view.findViewById(R.id.owner_state);
        owner_update = view.findViewById(R.id.save);
        owner_guest = view.findViewById(R.id.guests);
        markOnMap = view.findViewById(R.id.markOnMap);
        placesNear = view.findViewById(R.id.tags);
        dp = view.findViewById(R.id.dp);
        nearPlacesTag = view.findViewById(R.id.nearPlacesTag);
        check = view.findViewById(R.id.check);

        owner_update.setText(R.string.edit);
        disbale();

        tagsList = new ArrayList<>();
        getNearPlacesTags();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        owner_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (owner_update.getText().toString().equals("Edit")) {
                    enable();
                    owner_update.setText(R.string.update);
                } else {
                    disbale();
                    owner_update.setText(R.string.edit);
                    if (photoChanged)
                        uploadImage();
                    else
                        updateData();
                }
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Permission to read and write to storage !!");
                    builder.setMessage("This app needs permission to read and write images to storage.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
        });


        markOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGps();
            }
        });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLastLocation = location;
                        check.setVisibility(View.VISIBLE);
                        stopLocationUpdates();
                    }
                }
            }
        };

        placesNear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.tags, nearPlacesTag, false);
                String p = placesNear.getText().toString();
                TextView places = v.findViewById(R.id.tags);
                ImageView cut = v.findViewById(R.id.cut);

                places.setText(p);

                cut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View parent = (View) v.getParent();
                        nearPlacesTag.removeView(parent);
                    }
                });
                nearPlacesTag.addView(v);
            }
        });

        return view;
    }

    private void openImageIntent() {
        Intent intent = CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(120, 120)
                .getIntent(getActivity());
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void getNearPlacesTags() {
        tagsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray tags = jsonObject.getJSONArray("tags");
                    for (int i = 0; i < tags.length(); i++) {
                        String tag = tags.getJSONObject(i).getString("tag");
                        tagsList.add(tag);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, tagsList);
                placesNear.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void updateData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        ApplicationQueue.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void getGps() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        } else {
            getLocationDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationDialog();
            } else
                Toast.makeText(getActivity(), "Requested feature declined by user.", Toast.LENGTH_SHORT).show();

        }
    }

    private void getLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(1000);
    }

    private void uploadImage() {
        try {
            long time = System.currentTimeMillis();
            final String path = "_" + time + imgPath.substring(imgPath.lastIndexOf("."));
            new MultipartUploadRequest(getActivity(), "")
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
                            Toast.makeText(getActivity(), "Error Occured. Please Retry.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            updateData();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload();
        } catch (Exception e) {

        }
    }

    private void getLocationDialog() {
        getLocationRequest();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getAddressFromCoordinates();

                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(
                                        getActivity(),
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Toast.makeText(getActivity(), "Error Occured. Try Again.", Toast.LENGTH_SHORT).show();
                            } catch (ClassCastException e) {
                                Toast.makeText(getActivity(), "Error Occured. Try Again.", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(getActivity(), "Error Occured. Try Again.", Toast.LENGTH_SHORT).show();

                            break;
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:

                        getAddressFromCoordinates();

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity(), "Requested feature declined by user.", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }
                break;
            case REQUEST_CAMERA:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                        String filename = System.currentTimeMillis() + ".jpg";
                        File file = new File(getActivity().getFilesDir(), filename);

                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);

                        imgPath = file.getPath();
                        dp.setScaleType(ImageView.ScaleType.FIT_XY);
                        dp.setImageBitmap(bitmap);
                        photoChanged = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }


        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(locationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @SuppressLint("MissingPermission")
    private void getAddressFromCoordinates() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mLastLocation = location;


                        if (!Geocoder.isPresent()) {
                            Toast.makeText(getActivity(),
                                    "Network Error. Try again",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        startLocationUpdates();

                    }
                });

    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    private void disbale() {
        owner_name.setEnabled(false);
        owner_fathers_name.setEnabled(false);
        owner_house_number.setEnabled(false);
        owner_house_address.setEnabled(false);
        owner_pincode.setEnabled(false);
        owner_street.setEnabled(false);
        owner_city.setEnabled(false);
        owner_state.setEnabled(false);
        owner_update.setEnabled(false);
        owner_guest.setEnabled(false);
        markOnMap.setEnabled(false);
        placesNear.setEnabled(false);
        dp.setEnabled(false);
    }

    private void enable() {
        owner_name.setEnabled(true);
        owner_fathers_name.setEnabled(true);
        owner_house_number.setEnabled(true);
        owner_house_address.setEnabled(true);
        owner_pincode.setEnabled(true);
        owner_street.setEnabled(true);
        owner_city.setEnabled(true);
        owner_state.setEnabled(true);
        owner_update.setEnabled(true);
        owner_guest.setEnabled(true);
        markOnMap.setEnabled(true);
        placesNear.setEnabled(true);
        dp.setEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
