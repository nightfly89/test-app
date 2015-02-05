package youwowme.com.testapp.api.volley;

import android.app.Application;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;

import java.io.File;

public class VolleyRequestManager {
    private static final String DEFAULT_CACHE_DIR = "volley";

    private static VolleyRequestManager instance;

    public static VolleyRequestManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("VolleyRequestManager not initialized");
        }

        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            instance = new VolleyRequestManager(application);
        } else {
            throw new RuntimeException("VolleyRequestManager already initialized");
        }
    }


    private final Application application;
    private final RequestQueue requestQueue;

    private VolleyRequestManager(Application application) {
        this.application = application;

        File cacheDir = new File(application.getCacheDir(), DEFAULT_CACHE_DIR);

        HttpStack stack = new OkHttpStack();

        Network network = new VolleyNetwork(application, stack);

        requestQueue = new RequestQueue(new DiskBasedCache(cacheDir), network);
        requestQueue.start();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void cancelAll(String tag) {
        requestQueue.cancelAll(tag);
    }
}
