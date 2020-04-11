package com.example.vdemo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyVolley {
    private static MyVolley mInstance;
    private RequestQueue mrequestQueue;

    public MyVolley(Context context)
    {
        mrequestQueue=Volley.newRequestQueue(context.getApplicationContext());
    }
    public static synchronized MyVolley getInstance(Context context)
    {
        if(mInstance==null)
          mInstance= new MyVolley(context);
        return mInstance;
    }
    public RequestQueue getMrequestQueue()
    {
        return mrequestQueue;
    }
}
