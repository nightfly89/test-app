package youwowme.com.testapp.api.requests.people;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import youwowme.com.testapp.models.Group;

public class PeopleResponseData {
    @SerializedName("groups")
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }
}
