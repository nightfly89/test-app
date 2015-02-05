package youwowme.com.testapp.api.base;

public class ApiFailure {
    /**
     * Unknown error.
     */
    public static final int UNKNOWN_ERROR = -1;

    /**
     * Response status code 401 or 403
     */
    public static final int AUTH_ERROR = 1;

    /**
     * Response status code 5xx
     */
    public static final int SERVER_ERROR = 2;

    /**
     * Connection or Socket timeout
     */
    public static final int TIMEOUT_ERROR = 3;

    /**
     * No network errors
     */
    public static final int NETWORK_ERROR = 4;

    /**
     * No network errors -> No connection error
     */
    public static final int NO_CONNECTION_ERROR = 5;

    /**
     * Response parsing fail
     */
    public static final int PARSE_ERROR = 6;


    private int code;
    private int httpStatusCode;
    private String message;
    private Exception innerException;

    public ApiFailure() {
        code = -1;
        httpStatusCode = 0;
    }

    public ApiFailure(int code, int httpStatusCode, String message, Exception innerException) {
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
        this.innerException = innerException;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getMessage() {
        if(message != null) {
            return message;
        } else if(innerException != null) {
            return innerException.getMessage();
        } else {
            return "";
        }
    }

    public Exception getInnerException() {
        return innerException;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setInnerException(Exception innerException) {
        this.innerException = innerException;
    }
}
