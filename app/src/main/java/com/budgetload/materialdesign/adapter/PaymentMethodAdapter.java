package com.budgetload.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.budgetload.materialdesign.model.PaymentModel;
import com.budgetload.materialdesign.R;

import java.util.ArrayList;

/**
 * Created by andrewlaurienrsocia on 28/09/2016.
 */

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PaymentModel> mDataset;
    OnItemClickListener mItemClickListener;

    public PaymentMethodAdapter(Context context, ArrayList<PaymentModel> mDataset) {
        this.mContext = context;
        this.mDataset = mDataset;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView txtoutlet;
        private ImageView imgview;
        private TextView txtmode;
        private TextView txtmethod;
        public LinearLayout mainHolder;
        private TextView txtpaymentrate;
        private TextView txtdelivery;


        public ViewHolder(View itemView) {
            super(itemView);

            txtoutlet = (TextView) itemView.findViewById(R.id.txtgateway);
            imgview = (ImageView) itemView.findViewById(R.id.imageView11);
            txtmode = (TextView) itemView.findViewById(R.id.txtmode);
            txtmethod = (TextView) itemView.findViewById(R.id.txtmethod);
            mainHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            txtpaymentrate = (TextView) itemView.findViewById(R.id.txtpaymentrate);
            txtdelivery = (TextView) itemView.findViewById(R.id.txtdelivery);
            mainHolder.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paymentgateway, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentModel mylist = mDataset.get(position);
        holder.txtoutlet.setText(mylist.Outletname);
        holder.imgview.setImageResource(mylist.photoID);
        holder.txtmode.setText(mylist.mode);
        holder.txtmethod.setText(mylist.method);
        holder.txtpaymentrate.setText(""+mylist.paymentrate);

        if (mylist.delivery != null && mylist.delivery.length() != 0) {
            holder.txtdelivery.setText("Delivery: " + mylist.delivery);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
