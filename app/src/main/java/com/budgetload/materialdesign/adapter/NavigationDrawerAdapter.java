package com.budgetload.materialdesign.adapter;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.budgetload.materialdesign.DataBase.DataBaseHandler;
import com.budgetload.materialdesign.R;
import com.budgetload.materialdesign.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public static int mSelectedPosition = 1;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.myicon.setImageResource(current.getIcon());


        //Counter
        if (holder.txtcounter != null) {
            if (current.getCount() > 0) {
                holder.txtcounter.setVisibility(View.VISIBLE);
                holder.txtcounter.setText("" + current.getCount());
            } else {
                //Hide counter if == 0
                holder.txtcounter.setVisibility(View.GONE);
            }
        }


        holder.itemView.setSelected(mSelectedPosition == position);

        if (position == mSelectedPosition) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.navigationActive));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.navigationPress));
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }




    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView myicon;
        TextView txtcounter;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            myicon = (ImageView) itemView.findViewById(R.id.navicon);
            txtcounter = (TextView) itemView.findViewById(R.id.count);

            // Handle item click and set the selection
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redraw the old selection and the new
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = getLayoutPosition();
                    notifyItemChanged(mSelectedPosition);
                }
            });

        }
    }

    public void loadData() {

        DataBaseHandler db = new DataBaseHandler(context);
        // Notes and trash counts

        int countNotes = 0;

        Cursor mycursor = db.getunRead(db);
        try {
            countNotes = mycursor.getCount();
        } finally {
            if (mycursor != null && !mycursor.isClosed())
                mycursor.close();
        }


        // Set counter value to Navigation drawer items
        data.get(0).setCount(countNotes);
        notifyDataSetChanged();

    }


}
