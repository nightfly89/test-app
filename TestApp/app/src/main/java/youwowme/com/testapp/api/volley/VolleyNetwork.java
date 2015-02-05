package youwowme.com.testapp.api.volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpStatus;

import java.util.HashMap;

public class VolleyNetwork extends BasicNetwork {
    private Context context;

    public VolleyNetwork(Context context, HttpStack httpStack) {
        super(httpStack);
        this.context = context;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        if (request instanceof VolleyApiRequest
                && ((VolleyApiRequest) request).hasOfflineCacheResponse()
                && isNetworkAvailable(context)) {

            Cache.Entry entry = request.getCacheEntry();

            if (entry != null) {
                return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED, entry.data, new HashMap<String, String>(), true);
            }
        }

        return super.performRequest(request);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
