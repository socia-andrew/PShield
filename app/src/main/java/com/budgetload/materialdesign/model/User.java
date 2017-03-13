package com.budgetload.materialdesign.model;

/**
 * Created by rodrickregasa on 15/07/16.
 */
public class User {


    public String id = "", firstname = "", middlename = "", lastname = "", email = "",
            birthday = "", address = "", gender = "", mobile = "", groupcode = "", referrer = "", wholename;

    public User(String id, String firstname, String middlename, String lastname, String email,
                String birthday, String address, String gender, String mobile, String groupcode, String referrer, String wholename) {
        this.id = id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.gender = gender;
        this.mobile = mobile;
        this.groupcode = groupcode;
        this.referrer = referrer;
        this.wholename = wholename;
    }
}
