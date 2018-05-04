package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityList extends AppCompatActivity {

    DataBaseHandler db;
    ListView listView;
    EditText editSearch;
    String jsonArray;
    String PartnerID;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> comList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> searchCom = new ArrayList<HashMap<String, String>>();

    HashMap<String, String> commu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Initializing Database
        db = new DataBaseHandler(this);

        //Get partner ID
        PartnerID = db.getPartnerID(db);

        //remove auto fucos on the community
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Intent intent = getIntent();
        jsonArray = intent.getStringExtra("Community");

        try {
            JSONArray array = new JSONArray(jsonArray);


            JSONObject myobj;


            for (int i = 0; i < array.length(); i++) {
                commu = new HashMap<String, String>();

                myobj = array.getJSONObject(i);
                String com_id = myobj.getString("NetworkID");
                String com_name = myobj.getString("NetworkName");

                if (com_id.equals("REMITBOX")) {
                    com_name = "No Community";
                }


                commu.put("COMID", com_id);
                commu.put("COMNAME", com_name.toUpperCase());
                comList.add(commu);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.listView);
        editSearch = (EditText) findViewById(R.id.edtSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (count == 0) {
                    showCommunity();
                } else {
                    try {
                        JSONArray array = new JSONArray(jsonArray);


                        JSONObject myobj;
                        //clear the initial data set
                        searchCom.clear();

                        for (int i = 0; i < array.length(); i++) {

                            myobj = array.getJSONObject(i);
                            String com_id = myobj.getString("NetworkID");
                            String com_name = myobj.getString("NetworkName");
                            commu = new HashMap<String, String>();
                            commu.put("COMID", com_id);
                            commu.put("COMNAME", com_name);

                            //get the text in the EditText
                            String searchString = editSearch.getText().toString();
                            int textLength = searchString.length();
                            if (searchString.equalsIgnoreCase(com_name.substring(0, textLength))) {
                                searchCom.add(commu);

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    showSearchCommunity();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        showCommunity();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //final int myPosition = position;
                listView.setEnabled(false);


                String communitytitle = ((TextView) view
                        .findViewById(R.id.txtcommunitytitle)).getText().toString();
                String communityid = ((TextView) view
                        .findViewById(R.id.txtcommuntyid)).getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("ComID", communityid);
                returnIntent.putExtra("ComName", communitytitle);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();


            }

        });


    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

    }


    //endregion

    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void showCommunity() {

        ListAdapter adapter = new SimpleAdapter(getBaseContext(),
                comList, R.layout.list_community, new String[]{
                "COMNAME", "COMID"},
                new int[]{R.id.txtcommunitytitle, R.id.txtcommuntyid});

        listView.setAdapter(adapter);


    }


    public void showSearchCommunity() {


        ListAdapter adapter = new SimpleAdapter(getBaseContext(),
                searchCom, R.layout.list_community, new String[]{
                "COMNAME", "COMID"},
                new int[]{R.id.txtcommunitytitle, R.id.txtcommuntyid});

        listView.setAdapter(adapter);

    }

    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion


}






