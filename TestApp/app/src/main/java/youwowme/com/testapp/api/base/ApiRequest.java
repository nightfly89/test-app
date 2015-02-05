package youwowme.com.testapp.api.base;

public interface ApiRequest<T> {
    void setListener(ApiRequestListener<T> listener);

    void execute();

    void cancel();
}
