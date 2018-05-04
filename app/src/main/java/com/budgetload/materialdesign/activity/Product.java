package com.budgetload.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

public class Product extends AppCompatActivity {

    String brand;
    DataBaseHandler db;
    ListView listView;
    EditText editSearch;
    SimpleCursorAdapter myCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //remove auto fucos
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        db = new DataBaseHandler(this);

        Bundle b = getIntent().getExtras();
        brand = b.getString("Brand").toString();


        listView = (ListView) findViewById(R.id.listView);
        editSearch = (EditText) findViewById(R.id.edtSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (count == 0) {
                    showProduct();
                } else {
                    showSearchProduct();
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showSearchProduct();
            }
        });

        showProduct();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //final int myPosition = position;
                listView.setEnabled(false);


                String prodDescription = ((TextView) view
                        .findViewById(R.id.txtprodDescription)).getText().toString();
                String prodCode = ((TextView) view
                        .findViewById(R.id.txtprodcode)).getText().toString();
                String prodAmount = ((TextView) view
                        .findViewById(R.id.txtprodAmount)).getText().toString();
                String prodDiscount = ((TextView) view
                        .findViewById(R.id.txtdiscount)).getText().toString();


                Intent returnIntent = new Intent();
                returnIntent.putExtra("ProductDescription", prodDescription);
                returnIntent.putExtra("ProductCode", prodCode);
                returnIntent.putExtra("ProductAmount", prodAmount);
                returnIntent.putExtra("ProductDiscount", prodDiscount);
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

    public void showProduct() {


        // Toast.makeText(getBaseContext(),brand,Toast.LENGTH_LONG).show();

        Cursor mycursor = db.getProduct(db, brand);


        if (mycursor.getCount() > 0) {

            //Setup mapping from cursor to view fields;
            String[] FromFieldNames = new String[]
                    {
                            "Description", "Keyword", "Discount", "Amount"
                    };

            int[] toViewIDs = new int[]
                    {R.id.txtprodDescription, R.id.txtprodcode, R.id.txtdiscount, R.id.txtprodAmount};

            //create adapter to map coloums of the database to the elements of the UI
            myCursorAdapter =
                    new SimpleCursorAdapter(
                            this,                     //context
                            R.layout.list_product, //row_layout
                            mycursor,                 //Cursor
                            FromFieldNames,         //FromFields DataBaseColumns
                            toViewIDs                  //ToFields View IDs
                    );

        }


        listView.setAdapter(myCursorAdapter);


    }


    public void showSearchProduct() {


        if (editSearch.getText().length() > 0) {


            Cursor mycursor = db.getSearchProduct(db, brand, editSearch.getText().toString());

            // Toast.makeText(getBaseContext(), mycursor.getCount(), Toast.LENGTH_LONG).show();


            if (mycursor.getCount() > 0) {

                //Setup mapping from cursor to view fields;
                String[] FromFieldNames = new String[]
                        {
                                "Description", "Keyword", "Discount", "Amount"
                        };

                int[] toViewIDs = new int[]
                        {R.id.txtprodDescription, R.id.txtprodcode, R.id.txtdiscount, R.id.txtprodAmount};

                //create adapter to map coloums of the database to the elements of the UI
                myCursorAdapter =
                        new SimpleCursorAdapter(
                                this,                     //context
                                R.layout.list_product, //row_layout
                                mycursor,                 //Cursor
                                FromFieldNames,         //FromFields DataBaseColumns
                                toViewIDs                  //ToFields View IDs
                        );

            }


            listView.setAdapter(myCursorAdapter);
        }

    }


    //endregion

    //region THREADS
    //************************
    //THREADS
    //************************


    //endregion

}
