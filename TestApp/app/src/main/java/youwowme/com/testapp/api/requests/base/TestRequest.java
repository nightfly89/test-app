package youwowme.com.testapp.api.requests.base;

import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Map;

import youwowme.com.testapp.api.volley.VolleyApiRequest;
import youwowme.com.testapp.api.volley.VolleyRequestManager;

public class TestRequest<T> extends VolleyApiRequest<ApiResult<T>> {

    private final Class<T> responseType;

    public TestRequest(String path, Class<T> responseType) {
        super(Method.GET,  path, VolleyRequestManager.getInstance().getRequestQueue());

        this.responseType = responseType;
    }


    @Override
    protected ApiResult<T> parseResponse(String data, Map<String, String> headers) throws Exception {
        ApiResult<T> gdfApiResult = new ApiResult<>();

        JSONObject jsonObject = new JSONObject(data);
        GsonBuilder gsonBuilder = new GsonBuilder();



        gdfApiResult.data = gsonBuilder.create().fromJson(jsonObject.toString(), responseType);
        return gdfApiResult;
    }

}
