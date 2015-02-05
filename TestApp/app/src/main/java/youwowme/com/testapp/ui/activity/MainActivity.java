package youwowme.com.testapp.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import youwowme.com.testapp.R;
import youwowme.com.testapp.api.base.ApiFailure;
import youwowme.com.testapp.api.base.ApiRequestListener;
import youwowme.com.testapp.api.base.ApiResponse;
import youwowme.com.testapp.api.requests.base.ApiResult;
import youwowme.com.testapp.api.requests.people.PeopleRequest;
import youwowme.com.testapp.api.requests.people.PeopleResponseData;
import youwowme.com.testapp.models.Group;
import youwowme.com.testapp.models.Person;
import youwowme.com.testapp.ui.helper.IconHelper;


public class MainActivity extends ActionBarActivity {

    private static final String GROUP_LIST = "GROUP_LIST";

    private TextView tvPeople;
    private ProgressDialog progressDialog;
    private List<Group> groupList;
    private ExpandableListView lvPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        lvPeople = (ExpandableListView) findViewById(R.id.lv_people);
        progressDialog.setMessage("Loading data");

        PeopleRequest peopleRequest = new PeopleRequest();
        peopleRequest.setListener(new PeopleRequestListener());

        peopleRequest.execute();
        progressDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMessages(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void searchMessages(String query) {
        if (query.length() < 2) {
            Toast.makeText(this, getString(R.string.error_not_enough_characters), Toast.LENGTH_LONG).show();
        } else {
           String queryMatches = "";
           for (Group group : groupList) {
               for (Person person : group.getPeople()) {
                   if (person.getFirstName().toLowerCase().contains(query.toLowerCase()) || person.getLastName().toLowerCase().contains(query.toLowerCase())) {
                       queryMatches = queryMatches +  person.getFirstName() + " " + person.getLastName() + "\n";
                   }
               }
           }

            if (queryMatches.length() > 0) {
                queryMatches = getString(R.string.label_query_results) + queryMatches;
            } else {
                queryMatches = getString(R.string.label_no_results);
            }

            new AlertDialog.Builder(this).setMessage(queryMatches).setNeutralButton(getString(R.string.button_close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    private void initList() {
        PeopleAdapter peopleAdapter = new PeopleAdapter();
        lvPeople.setAdapter(peopleAdapter);

        int count = peopleAdapter.getGroupCount();
        for (int position = 1; position <= count; position++) {
            lvPeople.expandGroup(position - 1);
        }
    }

    //Api (if used in at least two different places this could further be made in another abstract layer and add a callback in the activity)
    class PeopleRequestListener implements ApiRequestListener<ApiResult<PeopleResponseData>> {
        @Override
        public void onSuccess(ApiResponse<ApiResult<PeopleResponseData>> response) {
            ApiResult<PeopleResponseData> apiResult = response.getData();
            groupList = apiResult.getData().getGroups();
            initList();
            progressDialog.dismiss();
        }

        @Override
        public void onFail(ApiFailure failure) {
            Toast.makeText(MainActivity.this, failure.getMessage() + " " + failure.getCode(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    //Adapter
    class PeopleAdapter extends BaseExpandableListAdapter{


        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groupList.get(groupPosition).getPeople().size();
        }

        @Override
        public Group getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public Person getChild(int groupPosition, int childPosition) {
            return groupList.get(groupPosition).getPeople().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private class GroupViewHolder {
            private TextView tvTitle;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.group_item, parent, false);

                holder = new GroupViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_header_title);

                convertView.setTag(holder);
            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }

            final String headerTitle = getGroup(groupPosition).getGroupName();

            holder.tvTitle.setText(headerTitle.toUpperCase());

            return convertView;
        }

        private class ChildViewHolder {
            private TextView tvName;
            private TextView tvStatus;
            private ImageView icon;
            private ImageView avatar;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ChildViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.person_item, parent, false);

                holder = new ChildViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_person_name);
                holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_person_status);
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_person_icon);
                holder.avatar = (ImageView) convertView.findViewById(R.id.iv_person_avatar);

                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            final Person person = getGroup(groupPosition).getPeople().get(childPosition);

            holder.tvName.setText(person.getFirstName() + " " + person.getLastName());
            holder.tvStatus.setText(person.getStatusMessage());
            holder.icon.setImageResource(IconHelper.getIconResource(person));
            holder.avatar.setImageResource(R.drawable.contacts_list_avatar_unknown);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
