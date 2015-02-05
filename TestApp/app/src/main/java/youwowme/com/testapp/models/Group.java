package youwowme.com.testapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("people")
    private List<Person> people;

    public String getGroupName() {
        return groupName;
    }

    public List<Person> getPeople() {
        return people;
    }
}
