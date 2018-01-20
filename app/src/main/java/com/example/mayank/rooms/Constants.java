package com.example.mayank.rooms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by mayan on 12/25/2017.
 */

public final class Constants {

    public static String HOUSE_OWNER_ID = "house_owner_id";
    public static String STUDENT_ID = "student_id";


    public static void showError(final VolleyError error, final String string, final Activity activity) {
        if (error.getClass() == TimeoutError.class) {
            Toast.makeText(activity, "Time Out. Please Reload.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setTitle("Error.");
            builder.setMessage("Some Error Occured. Please Report.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "bugs.codebuckets@gmail.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Error.");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, error + "\nMessage : "+ error.getMessage()+"\n" + string);
                            activity.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
