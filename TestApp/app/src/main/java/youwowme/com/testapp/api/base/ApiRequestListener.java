package youwowme.com.testapp.api.base;

public interface ApiRequestListener<T> {
    void onSuccess(ApiResponse<T> response);

    void onFail(ApiFailure failure);
}
