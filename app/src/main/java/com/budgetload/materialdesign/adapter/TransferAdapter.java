package com.budgetload.materialdesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.budgetload.materialdesign.ArrayList.TransferList;
import com.budgetload.materialdesign.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 11/24/15.
 */
public class TransferAdapter extends ArrayAdapter<TransferList> {

    private Activity activity;
    Context mcontext;
    DecimalFormat dec;

    public TransferAdapter(Activity activity, int resource, List<TransferList> list) {

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
            convertView = inflater.inflate(R.layout.list_transfer, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        TransferList mylist = getItem(position);

        if (holder.datetime != null) {

            holder.txnid.setText(mylist.getTxnNo());
            holder.datetime.setText(mylist.getDatetimeIN());
            holder.sender.setText(mylist.getSender());
            holder.receiver.setText(mylist.getReceiver());
            holder.amount.setText("P " + dec.format(mylist.getamount()) + "");
            holder.prevstock.setText("P " + dec.format(mylist.getprevstock()) + "");
            holder.prevconsumed.setText("P " + dec.format(mylist.getprevconsumed()) + "");
            holder.prevavailable.setText("P " + dec.format(mylist.getprevavailable()) + "");
            holder.prevbalance.setText("P " + dec.format(mylist.getprevbalance()) + "");
            holder.postbalance.setText("P " + dec.format(mylist.getpostbalance()) + "");
            holder.sendercommunity.setText(mylist.getSenderCommunity());
            holder.receivercommunity.setText(mylist.getReceiverCommunity());
        }

        return convertView;

    }

    private static class ViewHolder {

        private TextView txnid;
        private TextView datetime;
        private TextView sender;
        private TextView receiver;
        private TextView amount;
        private TextView prevstock;
        private TextView prevconsumed;
        private TextView prevavailable;
        private TextView prevbalance;
        private TextView postbalance;
        private TextView sendercommunity;
        private TextView receivercommunity;


        public ViewHolder(View v) {
            txnid = (TextView) v.findViewById(R.id.txnid);
            datetime = (TextView) v.findViewById(R.id.date);
            sender = (TextView) v.findViewById(R.id.sender);
            receiver = (TextView) v.findViewById(R.id.receiver);
            amount = (TextView) v.findViewById(R.id.amountval);
            prevstock = (TextView) v.findViewById(R.id.retailerprevstock);
            prevconsumed = (TextView) v.findViewById(R.id.retailerprevconsumed);
            prevavailable = (TextView) v.findViewById(R.id.retailerprevavailable);
            prevbalance = (TextView) v.findViewById(R.id.prebalance);
            postbalance = (TextView) v.findViewById(R.id.postbalance);
            sendercommunity = (TextView) v.findViewById(R.id.sendercommunity);
            receivercommunity = (TextView) v.findViewById(R.id.receivercommunity);
        }
    }


}
