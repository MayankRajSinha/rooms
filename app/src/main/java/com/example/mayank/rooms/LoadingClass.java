package com.example.mayank.rooms;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by mayan on 12/23/2017.
 */

public class LoadingClass {

    private Dialog dialog;
    private Context mContext;

    public LoadingClass(Context mContext) {
        this.mContext = mContext;
        dialog = new Dialog(mContext);
    }

    public static LoadingClass getInstance(Context context) {
        return new LoadingClass(context);
    }

    private void instantiateDialog() {
        dialog.setContentView(R.layout.progress_dialog);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public void showDialog() {
        instantiateDialog();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
