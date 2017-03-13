package com.budgetload.materialdesign.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.budgetload.materialdesign.Common.GlobalFunctions;
import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;

public class SettingPassword extends AppCompatActivity {

    DataBaseHandler db;
    Switch toggle;

    EditText inputpass;
    AlertDialog confimation;
    AlertDialog.Builder builder;
    String PasswordStatus = "";
    String confimpass = "";
    String CurrentPassword = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //for action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        db = new DataBaseHandler(this);

        //getPassword Status
        Cursor c = db.getPassword(db);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                PasswordStatus = c.getString(c.getColumnIndex("PasswordStatus"));
                CurrentPassword = c.getString(c.getColumnIndex("Password"));
            } while (c.moveToNext());
        }


        //COnfirmation for topUp
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        inputpass = new EditText(this);
        inputpass.setHint("Password");
        inputpass.setPadding(60, 10, 60, 10);
        inputpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputpass, 10, 100, 10, 20);
        builder.setCancelable(false);
        builder.setMessage("Password has been set for your security. Disabling password will allow you to do everything without validation. Are you sure to disable password? Enter your current password to validate.");


        builder.setPositiveButton("Disable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                confimpass = GlobalFunctions.getSha1Hex(inputpass.getText().toString());
                if ((PasswordStatus.equals("on") && confimpass.equals(CurrentPassword)) || PasswordStatus.equals("off") || PasswordStatus.equals("")) {
                    db.updatePasswordStatus(db, "off");
                    switchColor(false);
                } else {
                    Toast.makeText(getBaseContext(), "Password did not match.", 500).show();
                    toggle.setChecked(true);
                    inputpass.setText("");
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confimation.dismiss();
                toggle.setChecked(true);
                inputpass.setText("");
            }
        });
        confimation = builder.create();


        toggle = (Switch) findViewById(R.id.passwordstatswitch);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {
                    db.updatePasswordStatus(db, "on");
                    GetPassword();
                    switchColor(true);
                } else {
                    if (PasswordStatus.equals("on")) {
                        inputpass.setVisibility(View.VISIBLE);
                        confimation.show();
                        confimation.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#646464"));
                        confimation.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#19BD4E"));
                    } else {
                        db.updatePasswordStatus(db, "off");
                        switchColor(false);
                    }

//                    db.updatePasswordStatus(db,"off");
//                    switchColor(false);
                }
            }
        });


        Button changepass = (Button) findViewById(R.id.password);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iinent = new Intent(SettingPassword.this, PasswordChange.class);
                startActivity(iinent);
                finish();
            }
        });

        Cursor mycursor = db.getPassword(db);
        if (mycursor.getCount() > 0) {
            mycursor.moveToFirst();
            do {
                String passwordstat = mycursor.getString(mycursor.getColumnIndex("PasswordStatus"));

                if (passwordstat.equals("on")) {
                    toggle.setChecked(true);
                    switchColor(true);
                } else {
                    toggle.setChecked(false);
                    switchColor(false);
                }

            } while (mycursor.moveToNext());

        } else {
            toggle.setChecked(false);
            switchColor(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        //getPassword Status
        Cursor c = db.getPassword(db);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                PasswordStatus = c.getString(c.getColumnIndex("PasswordStatus"));
                CurrentPassword = c.getString(c.getColumnIndex("Password"));
            } while (c.moveToNext());
        }

        if (PasswordStatus.equals("on")) {
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //db.deletePreRegistration(db);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void GetPassword() {
        try {
            Cursor mycursor = db.getPassword(db);
            //already has signature in local
            if (mycursor.getCount() > 0) {
                mycursor.moveToFirst();
                do {
                    String passwordstat = mycursor.getString(mycursor.getColumnIndex("PasswordStatus"));
                    String password = mycursor.getString(mycursor.getColumnIndex("Password"));


                } while (mycursor.moveToNext());

            } else {
                Intent iinent = new Intent(SettingPassword.this, PasswordSetup.class);
                startActivity(iinent);


            }
        } catch (Exception e) {

        }

    }

    private void switchColor(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toggle.getThumbDrawable().setColorFilter(checked ? Color.BLUE : Color.WHITE, PorterDuff.Mode.MULTIPLY);
            toggle.getTrackDrawable().setColorFilter(!checked ? Color.BLACK : Color.WHITE, PorterDuff.Mode.MULTIPLY);
        }
    }

}
