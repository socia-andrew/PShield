package com.budgetload.materialdesign.adapter;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.budgetload.materialdesign.ArrayList.MyList;
import com.budgetload.materialdesign.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by andrewlaurienrsocia on 11/9/15.
 */
public class ContactListAdapter extends ArrayAdapter<MyList> {


    private Activity activity;
    Context mcontext;

    public ContactListAdapter(Activity activity, int resource, List<MyList> list) {

        super(activity, resource, list);
        this.activity = activity;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_contact, parent,
                    false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        MyList mylist = getItem(position);

        holder.name.setText(mylist.getContactname());
        holder.mobileno.setText(mylist.getMobileNumber());
        holder.contactid.setText(mylist.getContactid());
        holder.mynetwork.setText(mylist.getCarriername());

        String contid = mylist.getContactid();

        if (contid.equals("0")) {
            holder.myImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.contactinvite));
        } else {
            String firstLetter = String.valueOf(mylist.getContactname().charAt(0));
            ColorGenerator generator = ColorGenerator.MATERIAL;
            // generate random color
            int color = generator.getColor(getItem(position));
            TextDrawable drawable = TextDrawable.builder()//Color.parseColor("#2B60D0")
                    .buildRound(firstLetter.toUpperCase(), color); // radius in px
            holder.myImageView.setImageDrawable(drawable);
        }


        return convertView;

    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    private static class ViewHolder {

        private TextView name;
        private TextView mobileno;
        private ImageView myImageView;
        private TextView contactid;
        private TextView mynetwork;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.txtname);
            mobileno = (TextView) v.findViewById(R.id.txtcontactno);
            myImageView = (ImageView) v.findViewById(R.id.image_view);
            contactid = (TextView) v.findViewById(R.id.txtcontactid);
            mynetwork = (TextView) v.findViewById(R.id.lblNetwork);
        }
    }

    //Small Contact Photo
    public Bitmap openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = activity.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                }
            }
        } finally {
            cursor.close();
        }
        return null;

    }


    //Big Contact Photo
    public Bitmap openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);

//        Cursor cursor = activity.getContentResolver().query(displayPhotoUri,
//                new String[]{ContactsContract.Contacts.Photo.DISPLAY_PHOTO}, null, null, null);
//
//        if (cursor == null) {
//            return null;
//        }
//        try {
//            if (cursor.moveToFirst()) {
//                byte[] data = cursor.getBlob(0);
//                if (data != null) {
//                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
//                }
//            }
//        } finally {
//            cursor.close();
//        }
//        return null;


        try {
            AssetFileDescriptor fd =
                    activity.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");

            // return fd.createInputStream();

            Bitmap bmp = BitmapFactory.decodeStream(fd.createInputStream());

            return bmp;

        } catch (IOException e) {
            return null;
        }
    }

//
//    public Uri getPhotoUri(long contactid) {
//        try {
//            Cursor cur = activity.getContentResolver().query(
//                    ContactsContract.Data.CONTENT_URI,
//                    null,
//                    ContactsContract.Data.CONTACT_ID + "=" + contactid + " AND "
//                            + ContactsContract.Data.MIMETYPE + "='"
//                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
//                    null);
//            if (cur != null) {
//                if (!cur.moveToFirst()) {
//                    return null; // no photo
//                }
//            } else {
//                return null; // error in cursor process
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
//        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//    }
}
