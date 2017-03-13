package com.budgetload.materialdesign.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.budgetload.materialdesign.Constant.Constant;
import com.budgetload.materialdesign.DataBase.DataBaseInfo.DBInfo;
import com.budgetload.materialdesign.activity.GlobalVariables;
import com.budgetload.materialdesign.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by andrewlaurienrsocia on 11/12/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper implements Constant {

    public static final int database_version = 6;

    //region INITIALIZATION
    //**********************
    //Initializtion
    //**********************


    //Database version 3:
    //Need to saved the community to communitylist to implement the listview.


    public String CREATE_PREREGISTER = "CREATE TABLE " + DBInfo.PreRegistration + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DBInfo.Mobile + " TEXT," + DBInfo.Status + "," + DBInfo.Referrer + " TEXT)";
    public String CREATE_SESSION = "CREATE TABLE " + DBInfo.Session + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.SessionNo + " STRING," + DBInfo.DateLastUpdate + " TEXT)";
    public String CREATE_ACCOUNTPROFILE = "CREATE TABLE " + DBInfo.Account_Profile + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + DBInfo.AccountID + " TEXT," + DBInfo.FirstName + " TEXT," + DBInfo.MiddleName + " TEXT," + DBInfo.LastName + " TEXT," + DBInfo.Gender + " TEXT," + DBInfo.Birthday + " TEXT," + DBInfo.EmailAddress + " TEXT," + DBInfo.Address + " TEXT, " + DBInfo.Occupation + " TEXT," + DBInfo.Interest + " TEXT," + DBInfo.Referrer + " TEXT," + DBInfo.Mobile + " TEXT ," + DBInfo.SubDCode + " TEXT, " + DBInfo.GroupCode + " TEXT," + DBInfo.Exta1 + " TEXT," + DBInfo.Extra2 + " TEXT)";
    public String CREATE_PRODUCT_SIGNATURE = "CREATE TABLE " + DBInfo.Product_Signature + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.Network + " STRING," + DBInfo.Signature + " STRING," + DBInfo.LastUpdate + ")";
    public String CREATE_NETWORK_PREFIX = "CREATE TABLE " + DBInfo.Network_Prefix + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DBInfo.Prefix + " INTEGER, " + DBInfo.Brand + " TEXT)";
    public String CREATE_GLOBE_PRODUCT = "CREATE TABLE " + DBInfo.Globe_Product + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DBInfo.NetworkID + " TEXT," + DBInfo.MinAmt + " TEXT," + DBInfo.MaxAmt + " TEXT," + DBInfo.ProductCode + " TEXT," + DBInfo.Discount + " TEXT," + DBInfo.DiscountClass + " TEXT," + DBInfo.DenomType + " TEXT)";
    public String CREATE_SMART_PRODUCT = "CREATE TABLE " + DBInfo.Smart_Product + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.NetworkID + " TEXT," + DBInfo.Keyword + " TEXT, " + DBInfo.Amount + " TEXT," + DBInfo.TxnType + " TEXT," + DBInfo.Description + " TEXT ," + DBInfo.DiscountClass + " TEXT," + DBInfo.Discount + " TEXT," + DBInfo.Buddy + " TEXT," + DBInfo.TNT + " TEXT," + DBInfo.Bro + " TEXT)";
    public String CREATE_SUN_PRODUCT = "CREATE TABLE " + DBInfo.Sun_Product + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.NetworkID + " TEXT ," + DBInfo.ELPKeyword + " TEXT," + DBInfo.Amount + " TEXT," + DBInfo.TxnType + " TEXT," + DBInfo.Description + " TEXT," + DBInfo.DiscountClass + " TEXT," + DBInfo.Discount + " TEXT)";
    public String CREATE_TABLE_TOPUP_TXN = "CREATE TABLE " + DBInfo.TopUp_Txn + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.NetworkID + " TEXT," + DBInfo.TxnNo + " STRING, " + DBInfo.DateTimeIn + " TEXT," + DBInfo.Mobile + " TEXT," + DBInfo.TargetMobile + " TEXT," + DBInfo.ProductCode + " TEXT," + DBInfo.Amount + " TEXT," + DBInfo.TxnStatus + " TEXT," + DBInfo.ProductDescription + " TEXT, " + DBInfo.Discount + " TEXT," + DBInfo.PrevBalance + " TEXT," + DBInfo.PostBalance + " TEXT," + DBInfo.BudgetLoadRef + " TEXT)";
    public String CREATE_WALLET = "CREATE TABLE " + DBInfo.Wallet + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.Credit + " TEXT," + DBInfo.Debit + " TEXT," + DBInfo.Balance + " TEXT)";
    public String CREATE_STOCK = "CREATE TABLE " + DBInfo.STOCKTRANSFER_TXN + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + DBInfo.TxnNo + " TEXT," + DBInfo.Sender + " TEXT," + DBInfo.Receiver + " TEXT," + DBInfo.Amount + " TEXT," + DBInfo.TxnStatus + " TEXT," + DBInfo.DateTimeIn + " TEXT," + DBInfo.RetailerPrevStock + " TEXT," + DBInfo.RetailerPrevConsumed + " TEXT," + DBInfo.RetailerPrevAvailable + " TEXT," + DBInfo.PrevBalance + " TEXT," + DBInfo.PostBalance + " TEXT," + DBInfo.SenderCommunity + " TEXT," + DBInfo.ReceiverCommunity + " TEXT," + DBInfo.ReceiverName + " TEXT) ";
    public String CREATE_DEALER = "CREATE TABLE " + DBInfo.Network_Dealer + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.NetworkID + " TEXT," + DBInfo.NetworkName + " TEXT," + DBInfo.SubDCode + " TEXT," + DBInfo.Discount + " TEXT)";
    public String CREATE_COMMUNITYGROUP = "CREATE TABLE " + DBInfo.CommunityGroup + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + DBInfo.NetworkID + " TEXT," + DBInfo.GroupCode + " TEXT," + DBInfo.GroupName + " TEXT)";
    public String CREATE_PURCHASE = "CREATE TABLE " + DBInfo.Purchases + "(_id INTEGER PRIMARY KEY, " + DBInfo.TxnNo + " TEXT, " + DBInfo.TxnStatus + " TEXT, " + DBInfo.BranchID + " TEXT, " + DBInfo.Denoms + " TEXT, " + DBInfo.Amount + " TEXT," + DBInfo.DatePurchase + " TEXT)";
    public String CREATE_NOTIFICATIONS = "CREATE TABLE " + DBInfo.Notifications + "(_id INTEGER PRIMARY KEY," + DBInfo.Sender + " TEXT, " + DBInfo.Title + " TEXT," + DBInfo.Message + " TEXT," + DBInfo.DateSent + " TEXT," + DBInfo.Status + " TEXT," + DBInfo.Exta1 + " TEXT," + DBInfo.Extra2 + " TEXT)";
    public String CREATE_COMMUNITY = "CREATE TABLE " + DBInfo.Community + " (_id INTEGER PRIMARY KEY," + DBInfo.NetworkID + " TEXT," + DBInfo.NetworkName + " TEXT )";
    public String CREATE_COMMUNITY_LIST = "CREATE TABLE " + DBInfo.CommunityList + " (_id INTEGER PRIMARY KEY," + DBInfo.NetworkID + " TEXT," + DBInfo.NetworkName + " TEXT )";
    public String CREATE_PASSWORD = "CREATE TABLE " + DBInfo.PasswordSetUp + " (_id INTEGER PRIMARY KEY," + DBInfo.PasswordStatus + " TEXT," + DBInfo.Password + " TEXT )";


    //endregion

    //region CONSTRUCTOR
    //***********************
    //CONSTRUCTOR
    //***********************
    public DataBaseHandler(Context context) {
        super(context, DBInfo.DATABASE_NAME, null, database_version);

    }

    //endregion

    //region INHERITED METHODS
    //************************
    //INHERITED METHODS
    //************************


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PREREGISTER);
        db.execSQL(CREATE_SESSION);
        db.execSQL(CREATE_ACCOUNTPROFILE);
        db.execSQL(CREATE_PRODUCT_SIGNATURE);
        db.execSQL(CREATE_NETWORK_PREFIX);
        db.execSQL(CREATE_GLOBE_PRODUCT);
        db.execSQL(CREATE_SMART_PRODUCT);
        db.execSQL(CREATE_SUN_PRODUCT);
        db.execSQL(CREATE_TABLE_TOPUP_TXN);
        db.execSQL(CREATE_WALLET);
        db.execSQL(CREATE_STOCK);
        db.execSQL(CREATE_DEALER);
        db.execSQL(CREATE_PURCHASE);
        db.execSQL(CREATE_NOTIFICATIONS);
        db.execSQL(CREATE_COMMUNITY);
        db.execSQL(CREATE_PASSWORD);

        //db.execSQL(CREATE_COMMUNITY_LIST);
        //db.execSQL(CREATE_COMMUNITYGROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion <= 2) {
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.CommunityGroup);
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.CommunityList);
        }

        if (oldVersion <= 3) {
            //db.execSQL(CREATE_COMMUNITY_LIST);
            db.execSQL("DELETE FROM " + DBInfo.TopUp_Txn);
            db.execSQL("DELETE FROM " + DBInfo.STOCKTRANSFER_TXN);
            db.execSQL("DELETE FROM " + DBInfo.Purchases);
        }

        if (oldVersion < 4) {
            db.execSQL(CREATE_PASSWORD);
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.STOCKTRANSFER_TXN);
            db.execSQL(CREATE_STOCK);
        }


        if (oldVersion <= 5) {
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.STOCKTRANSFER_TXN);
            db.execSQL(CREATE_STOCK);
        }
        if (oldVersion <= 6) {
            db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Notifications);
            db.execSQL(CREATE_NOTIFICATIONS);
        }

    }

    //endregion

    //region CUSTOM METHODS
    //***************************
    //CUSTOM METHODS
    //***************************

    public String getSession(DataBaseHandler db) {

        String lastupdateddate = "";
        String result = "NONE";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor mycursor = sql.rawQuery("SELECT * FROM session", null);
        while (mycursor.moveToNext()) {
            GlobalVariables.SessionID = mycursor.getString(mycursor
                    .getColumnIndex("SessionNo"));
            lastupdateddate = mycursor.getString(mycursor
                    .getColumnIndex("DateLastUpdate"));
        }

        if (!lastupdateddate.equals("")) {
            // GET MINUTES LAPS
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String Now = dateFormat.format(date);
            Date startdate = new Date();
            Date enddate = new Date();
            try {
                if (lastupdateddate != null) {
                    enddate = dateFormat.parse(Now);
                    startdate = dateFormat.parse(lastupdateddate);
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.d("Date", "StartDate" + startdate + "");
            Log.d("Date", "EndDate" + enddate + "");
            long different = enddate.getTime() - startdate.getTime();
//            long secondsInMilli = 1000;
//            long minutesInMilli = secondsInMilli * 60;
//            long elapsedMinutes = different / minutesInMilli;
//
//            result = Long.toString(elapsedMinutes);
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            long elapsedMinutes = different / minutesInMilli;
            // different = different % minutesInMilli;
            Log.d("Minutes", "" + elapsedMinutes + "");
            result = Long.toString(elapsedMinutes);
        }
        return result;
    }

    public Cursor getIsRegistered(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * From " + DBInfo.PreRegistration + " LIMIT 1", null);
        return c;
    }


    public String getRegisteredStatus(DataBaseHandler db) {

        String myreturn = "";

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * From " + DBInfo.PreRegistration + " LIMIT 1", null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            //  do {
            myreturn = c.getString(c.getColumnIndex("Status"));
            //  } while (c.moveToNext());
        }

        return myreturn;

    }


//    public void saveSession(DataBaseHandler db, String session) {
//
//
//        Log.d("InsertSessionHere", session);
//
//        SQLiteDatabase sql = db.getWritableDatabase();
//
//        String d = "DELETE FROM session";
//        sql.execSQL(d);
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//
//        ContentValues value = new ContentValues();
//        value.put(DBInfo.SessionNo, session);
//        value.put(DBInfo.DateLastUpdate, dateFormat.format(date));
//
//        // inserting row
//        sql.insert(DBInfo.Session, null, value);
//
//        Log.d("InsertSessionHere", "" + value + "");
//
//
//    }

    public void savePreRegistration(DataBaseHandler db, String mobile, String referrer) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.Mobile, mobile);
        cv.put(DBInfo.Status, "ForVerification");
        cv.put(DBInfo.Referrer, referrer);

        sql.insert(DBInfo.PreRegistration, null, cv);

    }

//    public void deletePreRegistration(DataBaseHandler db) {
//
//        SQLiteDatabase sql = db.getReadableDatabase();
//        sql.execSQL("DELETE FROM " + DBInfo.PreRegistration + "");
//
//    }

    public void updatePreRegistration(DataBaseHandler db, String mobileno, String profiling) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.Status, profiling);

        // sql.update(DBInfo.TBLPreRegister, null, cv);
        sql.update(DBInfo.PreRegistration, cv, "Mobile='" + mobileno + "'", null);

    }

    public void saveProfile(DataBaseHandler db, String fname, String lname, String mname, String email, String address, String beerday, String occupation, String interest, String gender, String referrer, String mobile, String groupcode) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        sql.execSQL("DELETE FROM " + DBInfo.Account_Profile + "");

        cv.put(DBInfo.AccountID, GlobalVariables.SessionID);
        cv.put(DBInfo.FirstName, fname);
        cv.put(DBInfo.LastName, lname);
        cv.put(DBInfo.MiddleName, mname);
        cv.put(DBInfo.Gender, gender);
        cv.put(DBInfo.Birthday, beerday);
        cv.put(DBInfo.EmailAddress, email);
        cv.put(DBInfo.Address, address);
        cv.put(DBInfo.Occupation, occupation);
        cv.put(DBInfo.Interest, interest);
        cv.put(DBInfo.Referrer, referrer);
        cv.put(DBInfo.Mobile, mobile);
        cv.put(DBInfo.GroupCode, groupcode);


        sql.insert(DBInfo.Account_Profile, null, cv);


    }

    //Get all information about specific user
    public User getUser() {
        User user = null;
        SQLiteDatabase sql = this.getReadableDatabase();
        String wholename = "";
        Cursor cursor = sql.rawQuery("SELECT * FROM " + DBInfo.Account_Profile, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String id = cursor.getString(cursor.getColumnIndex("AccountID"));
                String firstname = cursor.getString(cursor.getColumnIndex("FirstName"));
                String middlename = cursor.getString(cursor.getColumnIndex("MiddleName"));
                String lastname = cursor.getString(cursor.getColumnIndex("LastName"));
                String email = cursor.getString(cursor.getColumnIndex("EmailAddress"));
                String birthday = cursor.getString(cursor.getColumnIndex("Birthday"));
                String address = cursor.getString(cursor.getColumnIndex("Address"));
                String gender = cursor.getString(cursor.getColumnIndex("Gender"));
                String mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
                String groupcode = cursor.getString(cursor.getColumnIndex("GroupCode"));
                String referrer = cursor.getString(cursor.getColumnIndex("Referrer"));
                if (!cursor.getString(cursor.getColumnIndex("FirstName")).equalsIgnoreCase("None") && !cursor.getString(cursor.getColumnIndex("FirstName")).equalsIgnoreCase(".")) {
                    wholename = cursor.getString(cursor.getColumnIndex("FirstName"));
                }
                if (!cursor.getString(cursor.getColumnIndex("MiddleName")).equalsIgnoreCase("None") && !cursor.getString(cursor.getColumnIndex("MiddleName")).equalsIgnoreCase(".")) {
                    wholename = wholename + " " + cursor.getString(cursor.getColumnIndex("MiddleName"));
                }
                if (!cursor.getString(cursor.getColumnIndex("LastName")).equalsIgnoreCase("None") && !cursor.getString(cursor.getColumnIndex("LastName")).equalsIgnoreCase(".")) {
                    wholename = wholename + " " + cursor.getString(cursor.getColumnIndex("LastName"));
                }
                user = new User(id, firstname, middlename, lastname, email, birthday, address, gender, mobile, groupcode, referrer, wholename);
                break;
            }
            cursor.close();
        }
        return user;
    }

    public Cursor getSignatures(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Product_Signature + "", null);

        return c;

    }

    public void saveSignature(DataBaseHandler db, String network, String signature) {


        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.Network, network);
        cv.put(DBInfo.Signature, signature);
        cv.put(DBInfo.LastUpdate, getDateTime());

        sql.insert(DBInfo.Product_Signature, null, cv);


    }

    public void saveSunProductCode(DataBaseHandler db, JSONArray myarray, String PartnerID) {

        SQLiteDatabase sql = db.getWritableDatabase();
        sql.beginTransaction();

        try {

            ContentValues cv = new ContentValues();

            JSONObject myobj;

            for (int i = 0; i < myarray.length(); i++) {

                try {

                    myobj = myarray.getJSONObject(i);

                    cv.put(DBInfo.NetworkID, PartnerID);
                    cv.put(DBInfo.ELPKeyword, myobj.getString("Keyword"));
                    cv.put(DBInfo.Amount, myobj.getString("Amount"));
                    cv.put(DBInfo.TxnType, myobj.getString("TransactionType"));
                    cv.put(DBInfo.Description, myobj.getString("Description"));
                    cv.put(DBInfo.DiscountClass, "");
                    cv.put(DBInfo.Discount, myobj.getString("Discount"));

                    sql.insert(DBInfo.Sun_Product, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }

    }

    public void saveGlobeProductCode(DataBaseHandler db, JSONArray myarray, String PartnerID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            JSONObject myobj;
            for (int i = 0; i < myarray.length(); i++) {
                try {
                    myobj = myarray.getJSONObject(i);
                    cv.put(DBInfo.NetworkID, PartnerID);
                    cv.put(DBInfo.MinAmt, myobj.getString("MinimumAmount"));
                    cv.put(DBInfo.MaxAmt, myobj.getString("MaximumAmount"));
                    cv.put(DBInfo.Discount, myobj.getString("Discount"));
                    cv.put(DBInfo.DenomType, myobj.getString("DenomType"));
                    cv.put(DBInfo.ProductCode, myobj.getString("ProductCode"));
                    sql.insert(DBInfo.Globe_Product, null, cv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }


    }

    public void saveSmartProductCode(DataBaseHandler db, JSONArray myarray, String PartnerID) {

        SQLiteDatabase sql = db.getWritableDatabase();
        sql.beginTransaction();

        try {

            ContentValues cv = new ContentValues();

            JSONObject myobj;

            for (int i = 0; i < myarray.length(); i++) {

                try {

                    myobj = myarray.getJSONObject(i);

                    cv.put(DBInfo.NetworkID, PartnerID);
                    cv.put(DBInfo.Keyword, myobj.getString("Keyword"));
                    cv.put(DBInfo.Amount, myobj.getString("Amount"));
                    cv.put(DBInfo.TxnType, myobj.getString("TransactionType"));
                    cv.put(DBInfo.Description, myobj.getString("Description"));
                    cv.put(DBInfo.Discount, myobj.getString("Discount"));
                    cv.put(DBInfo.Buddy, myobj.getString("SmartBuddy"));
                    cv.put(DBInfo.TNT, myobj.getString("TNT"));
                    cv.put(DBInfo.Bro, myobj.getString("SmartBro"));

                    sql.insert(DBInfo.Smart_Product, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }


    }

    public void saveNetPrefix(DataBaseHandler db, JSONArray myarray) {


        SQLiteDatabase sql = db.getWritableDatabase();
        sql.beginTransaction();

        try {

            ContentValues cv = new ContentValues();

            JSONObject myobj;

            for (int i = 0; i < myarray.length(); i++) {

                try {

                    myobj = myarray.getJSONObject(i);

                    cv.put(DBInfo.Prefix, myobj.getString("Prefix"));
                    cv.put(DBInfo.Brand, myobj.getString("Brand"));

                    sql.insert(DBInfo.Network_Prefix, null, cv);

                    Log.d("Save Prefix", "" + cv + "");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }


    }

    public int getPrefixCount(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Network_Prefix, null);
        return c.getCount();
    }

    public void deleteProdCode(DataBaseHandler db) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Sun_Product + "");
        sql.execSQL("DELETE FROM " + DBInfo.Smart_Product + "");
        sql.execSQL("DELETE FROM " + DBInfo.Globe_Product + "");

        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Sun_Product + "'");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Smart_Product + "'");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Globe_Product + "'");

    }

    public void DeletePrefix(DataBaseHandler db) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Network_Prefix + "");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Network_Prefix + "'");

    }

    public void DeleteGlobeProduct(DataBaseHandler db) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Globe_Product + "");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Globe_Product + "'");

    }

    public void DeleteSmartProduct(DataBaseHandler db) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Smart_Product + "");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Smart_Product + "'");

    }

    public void DeleteSunProduct(DataBaseHandler db) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Sun_Product + "");
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '"
                + DBInfo.Sun_Product + "'");

    }

    public String getNetworkPrefix(DataBaseHandler db, String prfix) {


        String myresult = "";

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Network_Prefix + " Where Prefix = " + prfix + "", null);


        Log.d("Query", "SELECT * FROM " + DBInfo.Network_Prefix + " Where Prefix = " + prfix + "");


        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                myresult = c.getString(c.getColumnIndex("Brand"));
            } while (c.moveToNext());
        }

        return myresult;


    }

    public Cursor getProduct(DataBaseHandler db, String brand) {

        String myquery = "";
        SQLiteDatabase sql = db.getReadableDatabase();

        if (brand.equalsIgnoreCase("BUDDY") || brand.equalsIgnoreCase("BUDDYBRO") || brand.equalsIgnoreCase("TNT") || brand.equalsIgnoreCase("Bro")) {

            String whereclause = "";

            if (brand.equals("BUDDY")) {
                whereclause = "Buddy = 'Y'";
            } else if (brand.equals("BUDDYBRO")) {
                whereclause = "(Bro = 'Y' OR Buddy='Y')";
            } else {
                whereclause = "TNT='Y'";
            }
            myquery = "SELECT _id,Description,Keyword,Discount,Amount FROM " + DBInfo.Smart_Product + " Where " + whereclause + " Order By _id";
        }

        if (brand.equalsIgnoreCase("SUN")) {
            myquery = "SELECT _id,Description,ELPKeyword as Keyword,Discount,Amount FROM " + DBInfo.Sun_Product + " Order By _id ASC";
        }

        if (brand.equalsIgnoreCase("GLOBE")) {
            myquery = "SELECT _id,ProductCode as Keyword, ProductCode as Description ,Discount,MaxAmt as Amount FROM " + DBInfo.Globe_Product + " Where DenomType ='SPECIAL LOAD' Order By _id ASC";
        }

        Log.d("Query", myquery);

        Cursor c = sql.rawQuery(myquery, null);


        return c;

    }

    public Cursor getSearchProduct(DataBaseHandler db, String brand, String searchkey) {

        String myquery = "";
        SQLiteDatabase sql = db.getReadableDatabase();

        if (brand.equalsIgnoreCase("BUDDY") || brand.equalsIgnoreCase("BUDDYBRO") || brand.equalsIgnoreCase("TNT") || brand.equalsIgnoreCase("Bro")) {

            String whereclause = "";

            if (brand.equals("BUDDY")) {
                whereclause = "Buddy = 'Y'";
            } else if (brand.equals("BUDDYBRO")) {
                whereclause = "(Bro = 'Y' OR Buddy='Y')";
            } else {
                whereclause = "TNT='Y'";
            }
            myquery = "SELECT _id,Description,Keyword,Discount,Amount FROM " + DBInfo.Smart_Product + " Where " + whereclause + " AND Description Like '%" + searchkey + "%' Order By Description ASC";
        }

        if (brand.equalsIgnoreCase("SUN")) {
            myquery = "SELECT _id,Description,ELPKeyword as Keyword,Discount,Amount FROM " + DBInfo.Sun_Product + " Where Description Like '%" + searchkey + "%' Order By Description ASC";
        }

        if (brand.equalsIgnoreCase("GLOBE")) {
            myquery = "SELECT _id,ProductCode as Keyword, ProductCode as Description ,Discount,MaxAmt as Amount FROM " + DBInfo.Globe_Product + " Where DenomType ='SPECIAL LOAD' AND Keyword Like '%" + searchkey + "%' Order By ProductCode ASC";
        }

        Log.d("Query", myquery);

        Cursor c = sql.rawQuery(myquery, null);


        return c;

    }

    public void saveTopUpPending(DataBaseHandler db, String brand, String txnno, String datetimein,
                                 String mobile, String targetmobile, String productcode, String productamount, String productype, String txnstatus, String BudgetLoadRef) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.TxnNo, txnno);
        cv.put(DBInfo.DateTimeIn, convertDateToMilliseconds(datetimein));
        cv.put(DBInfo.NetworkID, brand);
        cv.put(DBInfo.Mobile, mobile);
        cv.put(DBInfo.TargetMobile, targetmobile);
        cv.put(DBInfo.ProductCode, productcode);
        cv.put(DBInfo.Amount, productamount);
        cv.put(DBInfo.TxnStatus, txnstatus);
        cv.put(DBInfo.ProductDescription, productype);
        cv.put(DBInfo.BudgetLoadRef, BudgetLoadRef);
        cv.put(DBInfo.PostBalance, "0");
        cv.put(DBInfo.PrevBalance, "0");
        cv.put(DBInfo.Discount, "0");

        sql.insert(DBInfo.TopUp_Txn, null, cv);

    }

    public void saveCommunityList(DataBaseHandler db, JSONArray myarray) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        sql.execSQL("DELETE FROM " + DBInfo.CommunityList);
        sql.beginTransaction();

        try {
            JSONObject myobj;
            for (int i = 0; i < myarray.length(); i++) {
                try {
                    myobj = myarray.getJSONObject(i);
                    cv.put(DBInfo.NetworkID, myobj.getString("NetworkID"));
                    cv.put(DBInfo.NetworkName, myobj.getString("NetworkName"));

                    sql.insert(DBInfo.CommunityList, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sql.setTransactionSuccessful();
        } finally {
            sql.endTransaction();
        }

    }

    //Saving or
    public void saveTopTxn(DataBaseHandler db, JSONArray myarray, String brand) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String blref;

        JSONObject myobj;
        for (int i = 0; i < myarray.length(); i++) {
            try {
                myobj = myarray.getJSONObject(i);
                cv.put(DBInfo.TxnNo, myobj.getString("TxnNo"));
                cv.put(DBInfo.DateTimeIn, convertDateToMilliseconds(myobj.getString("DateTime")));
                cv.put(DBInfo.NetworkID, brand);
                cv.put(DBInfo.Mobile, myobj.getString("SourceMobTel"));
                cv.put(DBInfo.TargetMobile, myobj.getString("TargetMobTel"));
                cv.put(DBInfo.ProductCode, myobj.getString("ProductCode"));
                cv.put(DBInfo.Amount, myobj.getString("Amount"));
                cv.put(DBInfo.TxnStatus, myobj.getString("Status"));
                if (myobj.getString("ProductCode").equalsIgnoreCase("LD")) {
                    cv.put(DBInfo.ProductDescription, "Regular Load");
                } else {
                    cv.put(DBInfo.ProductDescription, myobj.getString("ProductDescription"));
                }

                if (myobj.getString("ProductDescription").equalsIgnoreCase(".")) {
                    cv.put(DBInfo.ProductDescription, myobj.getString("ProductCode"));
                } else {
                    cv.put(DBInfo.ProductDescription, myobj.getString("ProductDescription"));
                }

                cv.put(DBInfo.PrevBalance, myobj.getString("PrevBalance"));
                cv.put(DBInfo.PostBalance, myobj.getString("PostBalance"));
                if (myobj.getString("Status").equalsIgnoreCase("FAILED")) {
                    cv.put(DBInfo.Discount, 0);
                } else {
                    cv.put(DBInfo.Discount, myobj.getString("Discount"));
                }
                cv.put(DBInfo.BudgetLoadRef, myobj.getString("BudgetLoadRefNo"));
                blref = myobj.getString("BudgetLoadRefNo");
                Cursor c = sql.rawQuery("Select _id From " + DBInfo.TopUp_Txn + " Where BudgetLoadRef = '" + blref + "'", null);

                if (c.getCount() > 0) {
                    sql.update(DBInfo.TopUp_Txn, cv, "BudgetLoadRef='" + blref + "'", null);
                } else {
                    sql.insert(DBInfo.TopUp_Txn, null, cv);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //Unused for saving bulk transaction
    public void saveTopUpTxn(DataBaseHandler db, JSONArray myarray, String brand) {
        SQLiteDatabase sql = db.getWritableDatabase();
        //sql.execSQL("DELETE FROM " + DBInfo.TopUp_Txn + " Where NetworkID='" + brand + "'");
        sql.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            JSONObject myobj;
            for (int i = 0; i < myarray.length(); i++) {
                try {
                    myobj = myarray.getJSONObject(i);
                    cv.put(DBInfo.TxnNo, myobj.getString("TxnNo"));
                    cv.put(DBInfo.DateTimeIn, convertDateToMilliseconds(myobj.getString("DateTime")));
                    cv.put(DBInfo.NetworkID, brand);
                    cv.put(DBInfo.Mobile, myobj.getString("SourceMobTel"));
                    cv.put(DBInfo.TargetMobile, myobj.getString("TargetMobTel"));
                    cv.put(DBInfo.ProductCode, myobj.getString("ProductCode"));
                    cv.put(DBInfo.Amount, myobj.getString("Amount"));
                    cv.put(DBInfo.TxnStatus, myobj.getString("Status"));
                    cv.put(DBInfo.ProductDescription, myobj.getString("ProductDescription"));
                    cv.put(DBInfo.PrevBalance, myobj.getString("PrevBalance"));
                    cv.put(DBInfo.PostBalance, myobj.getString("PostBalance"));
                    cv.put(DBInfo.Discount, myobj.getString("Discount"));
                    sql.insert(DBInfo.TopUp_Txn, null, cv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sql.setTransactionSuccessful();
        } finally {
            sql.endTransaction();
        }
    }

    public void saveTransferTxn(DataBaseHandler db, JSONArray myarray) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM " + DBInfo.STOCKTRANSFER_TXN);
        sql.beginTransaction();
        try {
            // Log.d("InsertDatalength", "" + myarray.length() + "");
            ContentValues cv = new ContentValues();
            JSONObject myobj;
            for (int i = 0; i < myarray.length(); i++) {
                try {
                    myobj = myarray.getJSONObject(i);
                    cv.put(DBInfo.TxnNo, myobj.getString("TxnID"));
                    cv.put(DBInfo.Sender, myobj.getString("SubDistributor"));
                    cv.put(DBInfo.Receiver, myobj.getString("Retailer"));
                    cv.put(DBInfo.Amount, myobj.getString("Amount"));
                    cv.put(DBInfo.TxnStatus, "SUCCESSFUL");
                    cv.put(DBInfo.DateTimeIn, convertDateToMilliseconds(myobj.getString("DateTime")));
                    cv.put(DBInfo.RetailerPrevStock, myobj.getString("RetailerPrevStock"));
                    cv.put(DBInfo.RetailerPrevConsumed, myobj.getString("RetailerPrevConsumed"));
                    cv.put(DBInfo.RetailerPrevAvailable, myobj.getString("RetailerPrevAvailable"));
                    cv.put(DBInfo.PrevBalance, myobj.getString("PreBalance"));
                    cv.put(DBInfo.PostBalance, myobj.getString("PostBalance"));
                    cv.put(DBInfo.SenderCommunity, myobj.getString("SenderCommunity"));
                    cv.put(DBInfo.ReceiverCommunity, myobj.getString("ReceiverCommunity"));


                    if (myobj.getString("ReceiverName").trim().length() == 5) {
                        cv.put(DBInfo.ReceiverName, "Not Applicable");
                    } else {
                        cv.put(DBInfo.ReceiverName, myobj.getString("ReceiverName"));
                    }

                    // Log.d("InsertData", "" + cv + "");
                    sql.insert(DBInfo.STOCKTRANSFER_TXN, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sql.setTransactionSuccessful();
        } finally {
            sql.endTransaction();
        }
    }

    public void savePurchases(DataBaseHandler db, JSONArray myarray) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM " + DBInfo.Purchases);
        sql.beginTransaction();
        try {
            // Log.d("InsertDatalength", "" + myarray.length() + "");
            ContentValues cv = new ContentValues();
            JSONObject myobj;
            for (int i = 0; i < myarray.length(); i++) {
                try {
                    myobj = myarray.getJSONObject(i);
                    cv.put(DBInfo.TxnNo, myobj.getString("ReferenceNo"));
                    cv.put(DBInfo.TxnStatus, myobj.getString("TxnStatus"));
                    cv.put(DBInfo.BranchID, myobj.getString("TerminalID"));
                    cv.put(DBInfo.Amount, myobj.getString("Amount"));
                    cv.put(DBInfo.DatePurchase, convertDateToMilliseconds(myobj.getString("DateTime")));
                    //       Log.d("InsertData", "" + cv + "");
                    sql.insert(DBInfo.Purchases, null, cv);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sql.setTransactionSuccessful();
        } finally {
            sql.endTransaction();
        }
    }

    public void saveNewWallet(DataBaseHandler db, String Credit, String Debit, String Balance) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        sql.execSQL("DELETE From " + DBInfo.Wallet + "");
        cv.put(DBInfo.Credit, Credit);
        cv.put(DBInfo.Debit, Debit);
        cv.put(DBInfo.Balance, Balance);
        sql.insert(DBInfo.Wallet, null, cv);

    }

    public Cursor getWallet(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Wallet + "", null);
        return c;
    }

    public Cursor getTopUpData(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.TopUp_Txn + " Order By DateTimeIN Desc", null);
        return c;
    }

    public Cursor getTransfer(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.STOCKTRANSFER_TXN + " Order By DateTimeIN Desc", null);
        return c;
    }

    public Cursor getPurchaseData(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Purchases + " Order By DatePurchase Desc ", null);
        return c;
    }

    public Cursor getTopUpDetails(DataBaseHandler db, String txnno) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.TopUp_Txn + " Where " + DBInfo.TxnNo + " ='" + txnno + "' LIMIT 1", null);
        return c;
    }

    public Cursor getTransferDetails(DataBaseHandler db, String txnno) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.STOCKTRANSFER_TXN + " Where " + DBInfo.TxnNo + " ='" + txnno + "' LIMIT 1", null);
        return c;
    }

    public void saveDealer(DataBaseHandler db, JSONArray myarray, String PartnerID) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Network_Dealer);

        sql.beginTransaction();

        try {

            ContentValues cv = new ContentValues();

            JSONObject myobj;

            for (int i = 0; i < myarray.length(); i++) {

                try {

                    myobj = myarray.getJSONObject(i);

                    cv.put(DBInfo.NetworkID, PartnerID);
                    cv.put(DBInfo.NetworkName, myobj.getString("NetworkName"));
                    cv.put(DBInfo.SubDCode, myobj.getString("SubDCode"));
                    sql.insert(DBInfo.Network_Dealer, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }

    }

    public void saveGroupCode(DataBaseHandler db, JSONArray myarray, String PartnerID) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.CommunityGroup);

        sql.beginTransaction();

        try {

            ContentValues cv = new ContentValues();

            JSONObject myobj;

            for (int i = 0; i < myarray.length(); i++) {

                try {

                    myobj = myarray.getJSONObject(i);

                    cv.put(DBInfo.NetworkID, PartnerID);
                    cv.put(DBInfo.GroupCode, myobj.getString("GroupCode"));
                    cv.put(DBInfo.GroupName, myobj.getString("GroupName"));
                    sql.insert(DBInfo.CommunityGroup, null, cv);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            sql.setTransactionSuccessful();

        } finally {
            sql.endTransaction();
        }

    }

    public Cursor getCommunity(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();

        Cursor c = sql.rawQuery("SELECT * From " + DBInfo.CommunityGroup + "", null);

        return c;

    }


    public void updateProfile(DataBaseHandler db, String fname, String lname, String email, String address, String bday, String gender, String occupation, String interest, String group, String middlename, String dealertype, String referrer) {

        SQLiteDatabase sql = db.getWritableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.Account_Profile);

        ContentValues cv = new ContentValues();

        cv.put(DBInfo.FirstName, fname);
        cv.put(DBInfo.LastName, lname);
        cv.put(DBInfo.EmailAddress, email);
        cv.put(DBInfo.Address, address);
        cv.put(DBInfo.Birthday, bday);
        cv.put(DBInfo.Gender, gender);
        cv.put(DBInfo.Occupation, occupation);
        cv.put(DBInfo.Interest, interest);
        cv.put(DBInfo.GroupCode, group);
        cv.put(DBInfo.MiddleName, middlename);
        cv.put(DBInfo.SubDCode, dealertype);
        cv.put(DBInfo.Referrer, referrer);

        Log.d("InsertProfile", "" + cv + "");

        sql.insert(DBInfo.Account_Profile, null, cv);

    }

    public void saveReturnedHero(DataBaseHandler db, String fname, String lname, String email, String address, String mname, String gender, String interest, String occupation, String groupcode, String dealertype, String referrer, String beerday, String mobile) {


        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM " + DBInfo.Account_Profile + "");

        ContentValues cv = new ContentValues();

        cv.put(DBInfo.AccountID, GlobalVariables.SessionID);
        cv.put(DBInfo.FirstName, fname);
        cv.put(DBInfo.LastName, lname);
        cv.put(DBInfo.MiddleName, mname);
        cv.put(DBInfo.Gender, gender);
        cv.put(DBInfo.Birthday, beerday);
        cv.put(DBInfo.EmailAddress, email);
        cv.put(DBInfo.Address, address);
        cv.put(DBInfo.Occupation, occupation);
        cv.put(DBInfo.Interest, interest);
        cv.put(DBInfo.Referrer, referrer);
        cv.put(DBInfo.Mobile, mobile);
        cv.put(DBInfo.GroupCode, groupcode);
        cv.put(DBInfo.SubDCode, dealertype);


        sql.insert(DBInfo.Account_Profile, null, cv);


    }

    public Cursor getProfile(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Account_Profile
                + "", null);
        return c;

    }

    public Cursor getNetwork(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Product_Signature + "", null);
        return c;
    }

    public String getAccountGroupCode(DataBaseHandler db) {

        String myreturn = "none";

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Account_Profile + "", null);

        while (c.moveToNext()) {
            myreturn = c.getString(c.getColumnIndex("GroupCode"));
        }
        return myreturn;
    }

    public void saveNotification(DataBaseHandler db, String title, String message, String sender, String datesent, String status, String extra1) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBInfo.Title, title);
        cv.put(DBInfo.Message, message);
        cv.put(DBInfo.Sender, sender);
        cv.put(DBInfo.DateSent, datesent);
        cv.put(DBInfo.Status, status);
        cv.put(DBInfo.Exta1, extra1);

        Log.d("SaveNotificaiton", "" + cv + "");

        sql.insert(DBInfo.Notifications, null, cv);
    }

    public void DropAllTable(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();

        sql.execSQL("DELETE FROM " + DBInfo.PreRegistration);
        sql.execSQL("DELETE FROM " + DBInfo.Session);
        sql.execSQL("DELETE FROM " + DBInfo.Account_Profile);
        sql.execSQL("DELETE FROM " + DBInfo.Product_Signature);
        sql.execSQL("DELETE FROM " + DBInfo.Network_Prefix);
        sql.execSQL("DELETE FROM " + DBInfo.Globe_Product);
        sql.execSQL("DELETE FROM " + DBInfo.Smart_Product);
        sql.execSQL("DELETE FROM " + DBInfo.Sun_Product);
        sql.execSQL("DELETE FROM " + DBInfo.Wallet);
        sql.execSQL("DELETE FROM " + DBInfo.TopUp_Txn);
        sql.execSQL("DELETE FROM " + DBInfo.STOCKTRANSFER_TXN);
        sql.execSQL("DELETE FROM " + DBInfo.Purchases);
        sql.execSQL("DELETE FROM " + DBInfo.Network_Dealer);
        sql.execSQL("DELETE FROM " + DBInfo.Notifications);
        sql.execSQL("DELETE FROM " + DBInfo.Community);
        //sql.execSQL("DELETE FROM " + DBInfo.CommunityList);
        //sql.execSQL("DELETE FROM " + DBInfo.CommunityGroup);
        sql.execSQL("DELETE FROM SQLITE_SEQUENCE");

    }

    public Cursor getunRead(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Notifications + " Where " + DBInfo.Status + "='UNREAD'", null);
        return c;
    }

    public void updateNotification(DataBaseHandler db, String notificationid) {
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBInfo.Status, "READ");
        sql.update(DBInfo.Notifications, cv, "_id='" + notificationid + "'", null);
    }

    public Cursor getNotifications(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.Notifications + " Order By _id DESC ", null);
        return c;
    }

    public void deleteNotification(DataBaseHandler db, String txnid) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.delete(DBInfo.Notifications, "_id" + " = ?",
                new String[]{txnid});

    }

    public Cursor getNotificationDetails(DataBaseHandler db, String txnid) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * From " + DBInfo.Notifications + " Where _id = '" + txnid + "'", null);
        return c;
    }

    public void savePartnerID(DataBaseHandler db, String communityid, String communityname) {
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        sql.execSQL("DELETE FROM " + DBInfo.Community);
        cv.put(DBInfo.NetworkID, communityid);
        cv.put(DBInfo.NetworkName, communityname);
        sql.insert(DBInfo.Community, null, cv);
    }

    public String getPartnerID(DataBaseHandler db) {

        SQLiteDatabase sql = db.getReadableDatabase();
        String partnerid = "";
        Cursor c = sql.rawQuery("SELECT * From " + DBInfo.Community + "", null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                partnerid = c.getString(c.getColumnIndex("NetworkID"));
            }
        }
        return partnerid;


    }

    public Cursor getPassword(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + DBInfo.PasswordSetUp + "", null);

        return c;

    }

    public void SavePassword(DataBaseHandler db, String Password) {
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        sql.execSQL("DELETE FROM " + DBInfo.PasswordSetUp);
        cv.put(DBInfo.PasswordStatus, "on");
        cv.put(DBInfo.Password, Password);
        sql.insert(DBInfo.PasswordSetUp, null, cv);
    }

    public void deletePass(DataBaseHandler db) {
        SQLiteDatabase sql = db.getReadableDatabase();
        sql.execSQL("DELETE FROM " + DBInfo.PasswordSetUp);
    }

    public void updatePasswordStatus(DataBaseHandler db, String status) {
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBInfo.PasswordStatus, status);
        sql.update(DBInfo.PasswordSetUp, cv, "_id<>'3'", null);


    }

    //endregion

    //region Functions
    //**********************
    //FUNCTIONS
    //**********************

    public String convertDateToMilliseconds(String givenDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        String milliseconds = "";
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            milliseconds = Long.toString(timeInMilliseconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    // HH:mm:ss
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        // Date date = new Date();
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }


    //endregion


}
