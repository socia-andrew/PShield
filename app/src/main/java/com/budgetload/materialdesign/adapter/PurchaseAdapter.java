package com.budgetload.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.budgetload.materialdesign.ArrayList.PurchaseList;
import com.budgetload.materialdesign.R;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 11/25/15.
 */
public class PurchaseAdapter extends ArrayAdapter<PurchaseList> {

    private Activity activity;
    Context mcontext;
    DecimalFormat dec;

    public PurchaseAdapter(Activity activity, int resource, List<PurchaseList> list) {

        super(activity, resource, list);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        dec = new DecimalFormat("###,###,###,###.####");
        dec.setMinimumFractionDigits(2);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_purchases, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        PurchaseList mylist = getItem(position);

        if (holder.datetime != null) {
            String formatdate = convertDate(mylist.getDatepurchase(), "MM/dd/yyyy hh:mm:ss");

            holder.txnid.setText(mylist.getTxnno());
            holder.datetime.setText(formatdate);
            holder.branch.setText(mylist.getBranchid());
            holder.status.setText(mylist.getStatus());
            holder.amount.setText("P " + dec.format(mylist.getAmount()) + "");

        }

        return convertView;

    }

    private static class ViewHolder {

        private TextView txnid;
        private TextView datetime;
        private TextView branch;
        private TextView status;
        private TextView amount;

        public ViewHolder(View v) {

            txnid = (TextView) v.findViewById(R.id.txnno);
            datetime = (TextView) v.findViewById(R.id.datehour);
            branch = (TextView) v.findViewById(R.id.branchval);
            status = (TextView) v.findViewById(R.id.statusval);
            amount = (TextView) v.findViewById(R.id.amountval);


        }
    }

    public String convertDate(String dateInMilliseconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String dateString = formatter.format(new Date(Long.parseLong(dateInMilliseconds)));

        return dateString;
    }

}
