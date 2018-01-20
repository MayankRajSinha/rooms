package com.example.mayank.rooms.Front;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mayank.rooms.ApplicationQueue;
import com.example.mayank.rooms.R;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register_activity extends AppCompatActivity {

    private ImageView back;
    private EditText number;
    private Button otp;
    private Spinner registerAs;
    private static final String authkey = "155048A9L3FWxRPi59356550";
    private SmsVerifyCatcher smsVerifyCatcher;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        back = (ImageView) findViewById(R.id.back);
        number = (EditText) findViewById(R.id.numberEditext);
        otp = (Button) findViewById(R.id.sendOtp);
        registerAs = (Spinner) findViewById(R.id.registerAs);
        password = (EditText) findViewById(R.id.registerPassword);

        otp.setEnabled(false);
        otp.setBackgroundColor(Color.parseColor("#ffa4a2"));


        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    otp.setEnabled(true);
                    otp.setBackgroundColor(Color.parseColor("#af4448"));
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText().length() == 10)
                    askPermission();
                else {
                    Toast.makeText(register_activity.this, "Enter a valid number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
    Taking realtime permission to read the otp
     */
    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    0);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            String num = number.getText().toString();
            sendSMS(num);
            otpDialog(num);
        }
    }

    public void sendSMS(String number) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("control.msg91.com");
        builder.appendPath("api");
        builder.appendPath("sendotp.php");
        builder.appendQueryParameter("authkey", authkey);
        builder.appendQueryParameter("mobiles", "91" + number);
        builder.appendQueryParameter("sender", "vROOMS");
        builder.appendQueryParameter("otp_length", "6");

        String url = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("the response", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Mail error", error + "");
                    }
                });

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code1 = "";
        while (m.find()) {
            code1 = m.group(0);
        }
        return code1;
    }

    public void otpDialog(final String number) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_verify_dialogue);
        dialog.setCancelable(false);
        final EditText otp = (EditText) dialog.findViewById(R.id.enter_otp);
        Button verify = (Button) dialog.findViewById(R.id.verify);
        TextView resend = (TextView) dialog.findViewById(R.id.resendOTP);
        ImageView edit = (ImageView) dialog.findViewById(R.id.editNumber);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend(number);
            }
        });

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);
                otp.setText(code);

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString();
                verifySMS(number, code);

            }
        });
    }

    public void verifySMS(String number, String code) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("api.msg91.com");
        builder.appendPath("api");
        builder.appendPath("rest");
        builder.appendPath("verifyRequestOTP.php");
        builder.appendQueryParameter("authkey", authkey);
        builder.appendQueryParameter("mobiles", "91" + number);
        builder.appendQueryParameter("otp", code);

        String url = builder.build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("type");
                            if(status.equals("success")){
                               // switch ()
                            }else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Mail error", error + "");
                    }
                });

        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void Resend(String number) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("api.msg91.com");
        builder.appendPath("api");
        builder.appendPath("retryotp.php");
        builder.appendQueryParameter("authkey", authkey);
        builder.appendQueryParameter("mobiles", "91" + number);
        builder.appendQueryParameter("retrytype", "voice");

        final String url = builder.build().toString();

        AlertDialog.Builder uilder = new AlertDialog.Builder(register_activity.this);
        uilder.setTitle("Wait, You Will Get A Call.");
        uilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                makeOTPcall(url);
            }
        });
        AlertDialog dialog = uilder.create();
        dialog.show();
    }

    public void makeOTPcall(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("the response", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Mail error", error + "");
                    }
                });
        ApplicationQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void registerUser(){

    }
}
