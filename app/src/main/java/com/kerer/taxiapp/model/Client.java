package com.kerer.taxiapp.model;

import java.util.HashMap;

/**
 * Created by ivan on 03.01.17.
 */

public class Client {

    private String mName;
    private String mSurname;
    private String mPhone;
    private String uId;
    private HashMap<String, Order> mOrders;
    private boolean isBanned;


    public Client() {
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public HashMap<String, Order> getmOrders() {
        return mOrders;
    }

    public void setmOrders(HashMap<String, Order> mOrders) {
        this.mOrders = mOrders;
    }
}
