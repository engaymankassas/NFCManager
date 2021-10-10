package com.example.nfc;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.nfc.Models.DaoMaster;
import com.example.nfc.Models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class NFCApp extends Application {

    private DaoSession daoSession;
    private Database db;
    private RequestQueue mRequestQueue;
    private static NFCApp mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
       DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"nfc-db"); //The users-db here is the name of our database.
        db = helper.getWritableDb();
       daoSession = new DaoMaster(db).newSession();
    }

    public static synchronized NFCApp getInstance() {
        return mInstance;
    }


    public Database getDb() {
        return db;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public <T> void addToRequestQueue(Request<T> req) {

        req.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
}
