package com.kerer.taxiapp.model;

import com.google.firebase.database.Exclude;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 09.01.17.
 */

public class Order {
    private String mClientUid;
    private String mDriverUid;
    private String mOrigin;
    private String mDestination;
    private Timestamp mTimeStart;
    private Timestamp mTimeFinish;
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

    public Timestamp getmTimeStart() {
        return mTimeStart;
    }

    public void setmTimeStart(Timestamp mTimeStart) {
        this.mTimeStart = mTimeStart;
    }

    public Timestamp getmTimeFinish() {
        return mTimeFinish;
    }

    public void setmTimeFinish(Timestamp mTimeFinish) {
        this.mTimeFinish = mTimeFinish;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("mClientUid", mClientUid);
        result.put("mDriverUid", mDriverUid);
        result.put("mOrigin", mOrigin);
        result.put("mDestination", mDestination);
        result.put("mStatus", mStatus);

        return result;
    }
}
