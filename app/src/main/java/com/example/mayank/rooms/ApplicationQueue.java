package com.example.mayank.rooms;

import android.app.Application;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mayank on 12/23/2017.
 */


public class ApplicationQueue extends Application {
    private static ApplicationQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private ApplicationQueue(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized ApplicationQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApplicationQueue(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(5000, 10, 1));
        req.setShouldCache(false);
        getRequestQueue().add(req);
    }


}