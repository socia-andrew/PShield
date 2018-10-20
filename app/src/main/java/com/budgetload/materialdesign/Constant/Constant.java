package com.budgetload.materialdesign.Constant;

import com.budgetload.materialdesign.R;

/**
 * Created by andrewlaurienrsocia on 11/12/15.
 */
public interface Constant {


    //
    public static String versionCode = "27";
    public static long TIME_OUT_IN_SECONDS = 120;

    //*******************
    //DEVELOPMENT
    //*******************
    // TODO: comment when deploying to production
    public static String SESSIONURL = "http://dev-session.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PROFILEURL = "http://dev-profile.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String VERIFYURL = "http://dev-verify.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String REGISTERURL = "http://dev-register.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String UPDATEURL = "http://dev-update.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String LOADURL = "http://dev-load.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String COMMITURL = "http://dev-commit.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String TXNHISTURL = "http://dev-txnhist.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PURCHASEURL = "http://dev-purchase.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PRODUCTURL = "http://dev-product.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PRODUCTSYNCURL = "http://dev-sync.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String BALANCEURL = "http://dev-balance.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PREFIXURL = "http://dev-prefix.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String DEACTIVATE = "http://dev-deactivate.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String NOTIFICATION = "http://dev-notification.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String STOCKTRANSFER = "http://dev-transfer.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String COMMITSTOCKTRANSFER = "http://dev-committransfer.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String STOCKTRANSFERHISTORY = "http://dev-transfertxnhist.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String OTHERSURL = "http://dev-misc.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PURCHASECOMMIT = "http://dev-purchasecommit.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String PURCHASEREQUEST = "http://dev-purchaserequest.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String ACKNOWLEDGEMENT = "http://dev-acknowledgement.mypoweredup.com?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
    public static String DRAGONPAY = "http://test.dragonpay.ph/Pay.aspx";

    //******************* 
    // PRODUCTION 
    // *******************
//    public static String SESSIONURL = "https://session.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PROFILEURL = "https://profile.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String VERIFYURL = "https://verify.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String REGISTERURL = "https://register.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String UPDATEURL = "https://update.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String LOADURL = "https://load.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String COMMITURL = "https://commit.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String TXNHISTURL = "https://txnhist.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PURCHASEURL = "https://purchase.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PRODUCTURL = "https://product.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PRODUCTSYNCURL = "https://sync.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String BALANCEURL = "https://balance.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PREFIXURL = "https://prefix.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String DEACTIVATE = "https://deactivate.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String NOTIFICATION = "https://notification.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String STOCKTRANSFER = "https://transfer.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String COMMITSTOCKTRANSFER = "https://committransfer.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String STOCKTRANSFERHISTORY = "https://transfertxnhist.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String OTHERSURL = "https://misc.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String PURCHASECOMMIT = "";
//    public static String PURCHASEREQUEST = "https://purchaserequest.mybudgetload.com/?id=d6b27ee9de22e000700f0163e662ecba1d201c99";
//    public static String DRAGONPAY = "https://gw.dragonpay.ph/Pay.aspx";





    String onlinebank = "[{\"Outlet\":\"BDO Online Banking via DragonPay\",\"Mode\":\"1\"},{\"Outlet\":\"BPI Express Online/Mobile via DragonPay\",\"Mode\":\"1\"},{\"Outlet\":\"BPI (via DragonPay)\",\"Mode\":\"2\"},{\"Outlet\":\"Chinabank (via Dragonpay)\",\"Mode\":\"2\"}]";
//    String remittance = "[{\"Outlet\":\"Cebuana Lhuillier PeraPal  via DragonPay\",\"Mode\":\"CEBP\"},{\"Outlet\":\"LBC (via DragonPay)\",\"Mode\":\"LBC\"}]";
//    String gateway = "[{\"Gateway\":\"Online Banking\",\"Photo\":" + R.drawable.gw_onlinebank + ",\"Mode\":\"1\",\"method\":\"onlinebank\",\"paymentType\":\"OnlineBanking\",\"Delivery\":\"Within 24 hours\"}," +
//            "{\"Gateway\":\"Over the Counter Banking\",\"Photo\":" + R.drawable.gw_otcbank + ",\"Mode\":\"2\",\"method\":\"onlinebank\",\"paymentType\":\"OTCBanking\",\"Delivery\":\"Within 24 hours\"}," +
//            "{\"Gateway\":\"Remittance\",\"Photo\":" + R.drawable.gw_remittance + ",\"Mode\":\"4\",\"method\":\"remittance\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"\"}," +
//            "{\"Gateway\":\"7-Eleven\",\"Photo\": " + R.drawable.gw_eleven + ",\"Mode\":\"711\",\"method\":\"eleven\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"\"}," +
//            "{\"Gateway\":\"Robinsons Dpt. Store  \n (via DragonPay)\",\"Photo\":" + R.drawable.gw_robisons + ",\"Mode\":\"RDS\",\"method\":\"robinson\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"Within 48 hours\"}," +
//            "{\"Gateway\":\"SM Dpt. Store  \n (via DragonPay)\",\"Photo\":" + R.drawable.gw_sm + ",\"Mode\":\"SMR\",\"method\":\"sm\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"Within 48 hours\"}," +
//            "{\"Gateway\":\"Bayad Center  \n via DragonPay\",\"Photo\":" + R.drawable.ic_bayadcenter + ",\"Mode\":\"BAYD\",\"method\":\"remittance\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"Within 24 hours\"}," +
//            "{\"Gateway\":\"GCash\",\"Photo\":" + R.drawable.ic_glcash + ",\"Mode\":\"GCSH\",\"method\":\"remittance\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"Within 24 hours\"}," +
//            "{\"Gateway\":\"ECPay\",\"Photo\":" + R.drawable.ic_ecpay + ",\"Mode\":\"ECPY\",\"method\":\"remittance\",\"paymentType\":\"OTCNonbank\",\"Delivery\":\"Within 24 hours\"}]";


    String gateway = "[{\"Gateway\":\"Cebuana Lhuillier \n via DragonPay\",\"Mode\":\"CEBP\",\"Rate\":\"20\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.ic_cebuana + "}," +
            "{\"Gateway\":\"LBC  \n via DragonPay\",\"Mode\":\"LBC\",\"Rate\":\"20\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.ic_lbc + "}," +
            "{\"Gateway\":\"GCash \n via DragonPay\",\"Mode\":\"GCSH\",\"Rate\":\"10\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.ic_glcash + "}," +
            "{\"Gateway\":\"Bayad Center  \n via DragonPay\",\"Mode\":\"BAYD\",\"Rate\":\"20\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.ic_bayadcenter + "}," +
            "{\"Gateway\":\"ECPay \n via DragonPay\",\"Mode\":\"ECPY\",\"Rate\":\"20\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.ic_ecpay + "}," +
            "{\"Gateway\":\"Online Banking \n via DragonPay \",\"Mode\":\"1\",\"Rate\":\"10\",\"Delivery\":\"Within 24 hours\",\"method\":\"onlinebank\",\"Photo\":" + R.drawable.gw_onlinebank + "}," +
            "{\"Gateway\":\"Robinsons Dpt. Store\",\"Mode\":\"RDS\",\"Rate\":\"20\",\"Delivery\":\"Within 24 hours\",\"method\":\"remittance\",\"Photo\":" + R.drawable.gw_robisons + "}," +
            "{\"Gateway\":\"SM Dpt. Store\",\"Mode\":\"SMR\",\"Rate\":\"20\",\"Delivery\":\"Within 24 hours\",\"method\":\"remittance\",\"Photo\":" + R.drawable.gw_sm + "}," +
            "{\"Gateway\":\"Over the Counter Banking\",\"Mode\":\"2\",\"Rate\":\"15\",\"Delivery\":\"Within 24 hours\",\"method\":\"onlinebank\",\"Photo\":" + R.drawable.gw_otcbank + "}]";

    //  "{\"Gateway\":\"7-Eleven\",\"Mode\":\"711\",\"Rate\":\"15\",\"Delivery\":\"Real Time\",\"method\":\"remittance\",\"Photo\":" + R.drawable.gw_eleven + "}," +


    String Register = "R E G I S T E R";
    String miscellaneous = "M I S C E L L A N E O U S";
    String verify = "V E R I F Y  C O D E";
    String updateprofile = "U P D A T E  P R O F I L E";
    String getprofile = "G E T  P R O F I L E";
    String createsession = "C R E A T E  S E S S I O N";
    String getwallet = "G E T  W A L L E T  B A L A N C E";
    String topup = "T O P U P  L O A D";
    String commitopup = "C O M M I T  T O P U P";
    String transfer = "S T O C K  T R A N S F E R";
    String commitransfer = "C O M M I T  S T O C K  T R A N S F E R";
    String transfertxn = "S T O C K  T R A N S F E R  T R A N S A C T I O N";
    String purhasetxn = "P U R C H A S E  T R A N S A C T I O N";
    String topuptxn = "T O P U P  T R A N S A C T I O N";
    String purrequest = "P U R C H A S E R E Q U E S T";
    String defaultgateway = "DRAGONPAY";
    String cmdfetch = "FETCHAPIDETAILS";
    String idcode = "d6b27ee9de22e000700f0163e662ecba1d201c99";

}

