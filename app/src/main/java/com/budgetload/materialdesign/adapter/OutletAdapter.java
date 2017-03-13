package com.budgetload.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.budgetload.materialdesign.ArrayList.PaymentModel;
import com.budgetload.materialdesign.R;

import java.util.ArrayList;

/**
 * Created by andrewlaurienrsocia on 30/09/2016.
 */

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.ViewHolder> {

    Context mContext;
    ArrayList<PaymentModel> mDataset;
    OnItemClickListener mItemClickListener;

    public OutletAdapter(Context context, ArrayList<PaymentModel> mDataset) {
        this.mContext = context;
        this.mDataset = mDataset;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView txtoutlet;
        private TextView txtmode;
        public LinearLayout mainHolder;


        public ViewHolder(View itemView) {
            super(itemView);

            txtoutlet = (TextView) itemView.findViewById(R.id.txtgateway);
            txtmode = (TextView) itemView.findViewById(R.id.txtmodeid);
            mainHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paymentoutlet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OutletAdapter.ViewHolder holder, int position) {

        PaymentModel mylist = mDataset.get(position);


        holder.txtoutlet.setText(mylist.Outletname);
        holder.txtmode.setText(mylist.mode);


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



