package youwowme.com.testapp.api.requests.base;

public class ApiResult<T> {

    /*package*/ T data;

    public T getData() {
        return data;
    }
}