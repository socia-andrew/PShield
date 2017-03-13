package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.budgetload.materialdesign.Common.HideKeyboard;
import com.budgetload.materialdesign.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Ticket extends AppCompatActivity {


    ListView listView;
    String[] title;
    TextView tickettitle;
    ArrayList<HashMap<String, String>> comList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titleticket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HideKeyboard.hideKeyboard(Ticket.this);

        Bundle b = getIntent().getExtras();
        title = getResources().getStringArray(R.array.titleticket);

        listView = (ListView) findViewById(R.id.listView);
        tickettitle = ((TextView) findViewById(R.id.txttickettitle));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listView.setEnabled(false);
                String selected = parent.getItemAtPosition(position).toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("TicketTitle", selected);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();


            }

        });
        showTicketTitle();

    }

    //region TRIGGERS
    //************************
    //TRIGGERS
    //************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //endregion


    //region FUNCTIONS
    //************************
    //FUNCTIONS
    //************************

    public void showTicketTitle() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_titleticket, R.id.txttickettitle, title);
        listView.setAdapter(adapter);


    }


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion




}
