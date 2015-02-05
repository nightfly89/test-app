package youwowme.com.testapp.ui.helper;

import youwowme.com.testapp.R;
import youwowme.com.testapp.models.Person;

public class IconHelper {

    public static final String ICON_ONLINE = "online";
    public static final String ICON_OFFLINE = "offline";
    public static final String ICON_BUSY = "busy";
    public static final String ICON_AWAY = "away";
    public static final String ICON_CALL_FORWARD = "callforwarding";

    public static int getIconResource(Person person) {
        int imgResource;
        switch (person.getStatusIcon()) {
            case ICON_ONLINE:
                imgResource = R.drawable.contacts_list_status_online;
                break;
            case ICON_OFFLINE:
                imgResource = R.drawable.contacts_list_status_offline;
                break;
            case ICON_BUSY:
                imgResource = R.drawable.contacts_list_status_busy;
                break;
            case ICON_AWAY:
                imgResource = R.drawable.contacts_list_status_away;
                break;
            case ICON_CALL_FORWARD:
                imgResource = R.drawable.contacts_list_call_forward;
                break;
            default:
                imgResource = R.drawable.contacts_list_status_pending;
        }
        return imgResource;
    }

}
