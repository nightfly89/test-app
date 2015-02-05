package youwowme.com.testapp;

import android.app.Application;

import youwowme.com.testapp.api.volley.VolleyRequestManager;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VolleyRequestManager.init(this);
    }
}
