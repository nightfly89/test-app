package youwowme.com.testapp.api.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;
import java.util.concurrent.Executor;

import youwowme.com.testapp.api.base.ApiFailure;
import youwowme.com.testapp.api.base.ApiRequest;
import youwowme.com.testapp.api.base.ApiRequestListener;
import youwowme.com.testapp.api.base.ApiResponse;

public abstract class VolleyApiRequest<T> extends Request<ApiResponse<T>> implements
        ApiRequest<T> {

    private ApiRequestListener<T> listener;
    private Executor responseDeliveryExecutor;

    private final RequestQueue requestQueue;

    private boolean offlineCacheResponse = false;

    public VolleyApiRequest(int method, String url, RequestQueue requestQueue) {
        super(method, url, null);

        this.requestQueue = requestQueue;
    }

    @Override
    public void setListener(ApiRequestListener<T> requestListener) {
        this.listener = requestListener;
    }

    public void setResponseDeliveryExecutor(Executor responseDeliveryExecutor) {
        this.responseDeliveryExecutor = responseDeliveryExecutor;
    }

    public boolean hasOfflineCacheResponse() {
        return offlineCacheResponse;
    }

    public void setOfflineCacheResponse(boolean offlineCacheResponse) {
        this.offlineCacheResponse = offlineCacheResponse;
    }

    @Override
    public void execute() {
        requestQueue.add(this);
    }

    @Override
    protected Response<ApiResponse<T>> parseNetworkResponse(NetworkResponse networkResponse) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setHeaders(networkResponse.headers);

        try {
            String responseStringData = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            apiResponse.setData(parseResponse(responseStringData, networkResponse.headers));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(apiResponse, getCacheMetadata(apiResponse, networkResponse));
    }

    protected abstract T parseResponse(String data, Map<String, String> headers) throws Exception;

    protected Cache.Entry getCacheMetadata(ApiResponse<T> apiResponse, NetworkResponse networkResponse) {
        return HttpHeaderParser.parseCacheHeaders(networkResponse);
    }


    @Override
    protected void deliverResponse(final ApiResponse<T> response) {
        if (listener != null) {
            if (responseDeliveryExecutor == null) {
                listener.onSuccess(response);
            } else {
                responseDeliveryExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(response);
                    }
                });
            }
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (listener != null) {
            listener.onFail(decodeVolleyError(error));
        }
    }

    private ApiFailure decodeVolleyError(VolleyError volleyError) {
        ApiFailure apiFailure = new ApiFailure();

        apiFailure.setInnerException(volleyError);
        if (volleyError.networkResponse != null) {
            apiFailure.setHttpStatusCode(volleyError.networkResponse.statusCode);
        }

        if (volleyError instanceof AuthFailureError) {
            apiFailure.setCode(ApiFailure.AUTH_ERROR);
        } else if (volleyError instanceof NetworkError) {
            if (volleyError instanceof NoConnectionError) {
                apiFailure.setCode(ApiFailure.NO_CONNECTION_ERROR);
            } else {
                apiFailure.setCode(ApiFailure.NETWORK_ERROR);
            }
        } else if (volleyError instanceof ServerError) {
            apiFailure.setCode(ApiFailure.SERVER_ERROR);
        } else if (volleyError instanceof TimeoutError) {
            apiFailure.setCode(ApiFailure.TIMEOUT_ERROR);
        } else if (volleyError instanceof ParseError) {
            apiFailure.setCode(ApiFailure.PARSE_ERROR);
        } else {
            apiFailure.setCode(ApiFailure.UNKNOWN_ERROR);
        }

        return apiFailure;
    }
}
