package com.budgetload.materialdesign.model;

/**
 * Created by Ravi on 29/07/15.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int myicon;
    private int count;
    public NavDrawerItem() {

    }

//    public NavDrawerItem(boolean showNotify, String title,int myicon) {
//        this.showNotify = showNotify;
//        this.title = title;
//        this.myicon = myicon;
//    }
//
//    public NavDrawerItem(boolean showNotify, String title,int myicon,int count) {
//        this.showNotify = showNotify;
//        this.title = title;
//        this.myicon = myicon;
//        this.count = count;
//    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(int myicon){
        this.myicon = myicon;
    }

    public int getIcon(){
        return myicon;
    }

    public void setCount(int mycount){
        this.count = mycount;
    }

    public int getCount(){
        return  this.count;
    }


}
