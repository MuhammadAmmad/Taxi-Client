package com.kerer.taxiapp.model;

/**
 * Created by ivan on 09.01.17.
 */

public class Order {
    private String mClientUid;
    private String mDriverUid;
    private String mOrigin;
    private String mDestination;
    private int mStatus;

    public Order() {
    }

    public String getmClientUid() {
        return mClientUid;
    }

    public void setmClientUid(String mClientUid) {
        this.mClientUid = mClientUid;
    }

    public String getmDriverUid() {
        return mDriverUid;
    }

    public void setmDriverUid(String mDriverUid) {
        this.mDriverUid = mDriverUid;
    }

    public String getmOrigin() {
        return mOrigin;
    }

    public void setmOrigin(String mOrigin) {
        this.mOrigin = mOrigin;
    }

    public String getmDestination() {
        return mDestination;
    }

    public void setmDestination(String mDestination) {
        this.mDestination = mDestination;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }
}
