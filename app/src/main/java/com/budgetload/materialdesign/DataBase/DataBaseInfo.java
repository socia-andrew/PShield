package com.budgetload.materialdesign.DataBase;

import android.provider.BaseColumns;

/**
 * Created by andrewlaurienrsocia on 11/12/15.
 */
public class DataBaseInfo {

    public static class DBInfo implements BaseColumns {

        //DataBase Name
        public static final String DATABASE_NAME = "budgetload_app";


        //TableName
        public static final String PreRegistration = "PreRegistration";
        public static final String Session = "Session";
        public static final String Account_Profile = "Account_Profile";
        public static final String Product_Signature = "Product_Signature";
        public static final String Network_Prefix = "Network_Prefix";
        public static final String Globe_Product = "Globe_Product";
        public static final String Smart_Product = "Smart_Product";
        public static final String Sun_Product = "Sun_Product";
        public static final String TopUp_Txn = "TopUp_Txn";
        public static final String Wallet = "Wallet";
        public static final String STOCKTRANSFER_TXN = "Stock_transfer";
        public static final String Network_Dealer = "Network_Dealer";
        public static final String CommunityGroup = "Community_Group";
        public static final String Purchases = "Purchases";
        public static final String Notifications = "Notifcations";
        public static final String Community = "Community";
        public static final String CommunityList = "CommunityList";
        public static final String PasswordSetUp = "Password";
        public static final String AllowedNumbers = "AllowedNumbers";


        //Fields

        public static final String Mobile = "Mobile";
        public static final String Status = "Status";

        public static final String SessionNo = "SessionNo";
        public static final String DateLastUpdate = "DateLastUpdate";


        public static final String AccountID = "AccountID";
        public static final String FirstName = "FirstName";
        public static final String LastName = "LastName";
        public static final String MiddleName = "MiddleName";
        public static final String Gender = "Gender";
        public static final String Birthday = "Birthday";
        public static final String EmailAddress = "EmailAddress";
        public static final String Address = "Address";
        public static final String Occupation = "Occupation";
        public static final String Interest = "Interest";
        public static final String Referrer = "Referrer";


        public static final String Network = "Network";
        public static final String Signature = "Signature";
        public static final String LastUpdate = "LastUpdate";

        public static final String Prefix = "Prefix";
        public static final String Brand = "Brand";
        public static final String Min = "Min";
        public static final String Max = "Max";


        public static final String NetworkID = "NetworkID";
        public static final String MinAmt = "MinAmt";
        public static final String MaxAmt = "MaxAmt";
        public static final String ProductCode = "ProductCode";
        public static final String Discount = "Discount";
        public static final String DiscountClass = "DiscountClass";
        public static final String Description = "Description";
        public static final String DenomType = "DenomType";


        public static final String Keyword = "Keyword";
        public static final String Amount = "Amount";
        public static final String TxnType = "TxnType";
        public static final String Buddy = "Buddy";
        public static final String TNT = "TNT";
        public static final String Bro = "Bro";

        public static final String ELPKeyword = "ELPKeyword";

        public static final String TxnNo = "TxnNo";
        public static final String DateTimeIn = "DateTimeIN";
        public static final String TargetMobile = "TargetMobile";
        public static final String TxnStatus = "TxnStatus";
        public static final String ProductDescription = "ProductDescription";
        public static final String PrevBalance = "PrevBalance";
        public static final String PostBalance = "PostBalance";
        public static final String BudgetLoadRef = "BudgetLoadRef";


        public static final String Credit = "Credit";
        public static final String Debit = "Debit";
        public static final String Balance = "Balance";

        public static final String Sender = "Sender";
        public static final String Receiver = "Receiver";
        public static final String RetailerPrevStock = "RetailerPrevStock";
        public static final String RetailerPrevConsumed = "RetailerPrevConsumed";
        public static final String RetailerPrevAvailable = "RetailerPrevAvailable";

        public static final String NetworkName = "NetworkName";
        public static final String SubDCode = "SubDCode";

        public static final String GroupCode = "GroupCode";
        public static final String GroupName = "GroupName";

        public static final String BranchID = "BranchID";
        public static final String Denoms = "Denoms";
        public static final String DatePurchase = "DatePurchase";


        public static final String Title = "Title";
        public static final String Message = "Message";
        public static final String DateSent = "DateSent";


        //Extra Fields

        public static final String Exta1 = "Extra1";
        public static final String Extra2 = "Extra2";
        public static final String Extra3 = "Extra3";
        public static final String Extra4 = "Extra4";
        public static final String Notes1 = "Notes1";
        public static final String Notes2 = "Notes2";

        //updates for password
        public static final String PasswordStatus = "PasswordStatus";
        public static final String Password = "Password";

        //update additional fields for stock transfer
        public static final String SenderCommunity = "SenderCommunity";
        public static final String ReceiverCommunity = "ReceiverCommunity";
        public static final String ReceiverName = "ReceiverName";


    }
}
