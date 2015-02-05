package youwowme.com.testapp.api.requests.people;

import youwowme.com.testapp.api.requests.base.TestRequest;

public class PeopleRequest extends TestRequest<PeopleResponseData> {
    private final static String PATH_PEOPLE = "http://downloadapp.youwow.me/iPhone/iOSTest/contacts.json";

    public PeopleRequest() {
        super(PATH_PEOPLE, PeopleResponseData.class);
    }
}
