package youwowme.com.testapp.models;

import com.google.gson.annotations.SerializedName;

public class Person {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("statusIcon")
    private String statusIcon;

    @SerializedName("statusMessage")
    private String statusMessage;

    private String avatar = "default_avatar.png";

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatusIcon() {
        return statusIcon;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getAvatar() {
        return avatar;
    }
}
