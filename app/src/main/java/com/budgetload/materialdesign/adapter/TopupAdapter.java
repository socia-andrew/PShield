package com.budgetload.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.budgetload.materialdesign.model.TopupList;
import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 11/24/15.
 */
public class TopupAdapter extends ArrayAdapter<TopupList> {

    private Activity activity;
    Context mcontext;
    DecimalFormat dec;

    public TopupAdapter(Activity activity, int resource, List<TopupList> list) {

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
            convertView = inflater.inflate(R.layout.list_topup, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        TopupList mylist = getItem(position);


        if (holder.datetime != null) {

            holder.txnid.setText(mylist.getTxnid());
            holder.datetime.setText(mylist.getDatetime());
            holder.mobile.setText(mylist.getMobile());
            holder.status.setText(mylist.getStatus());
            holder.blrefno.setText(mylist.getBlRefNo());

            if (mylist.getStatus().equalsIgnoreCase("PENDING")) {
                holder.status.setTextColor(Color.parseColor("#FF0000"));
            }
            if (mylist.getStatus().equalsIgnoreCase("FAILED")) {
                holder.status.setTextColor(Color.parseColor("#FF0000"));
            }
            if(mylist.getStatus().equalsIgnoreCase("SUCCESSFUL")){
                holder.status.setTextColor(Color.parseColor("#006DD9"));
            }
            holder.amount.setText("P " + dec.format(mylist.getAmount()) + "");

        }

        return convertView;

    }

    private static class ViewHolder {

        private TextView txnid;
        private TextView datetime;
        private TextView mobile;
        private TextView status;
        private TextView amount;
        private TextView blrefno;

        public ViewHolder(View v) {
            txnid = (TextView) v.findViewById(R.id.txnno);
            datetime = (TextView) v.findViewById(R.id.datehour);
            mobile = (TextView) v.findViewById(R.id.mobilenumberval);
            status = (TextView) v.findViewById(R.id.statusvalue);
            amount = (TextView) v.findViewById(R.id.amountval);
            blrefno = (TextView) v.findViewById(R.id.budgetloadref);
        }
    }


}
