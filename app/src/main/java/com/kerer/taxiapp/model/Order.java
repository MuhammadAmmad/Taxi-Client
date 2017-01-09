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
    private float mDistance;
    private float mPayment;
    private Timestamp mTimeStart;
    private Timestamp mTimeFinish;
    private int mStatus;
    private String mKey;

    public Order() {
    }

    public Order(String mClientUid, String mDriverUid, String mOrigin, String mDestination, float mDistance, float mPayment, int mStatus, String mKey) {
        this.mClientUid = mClientUid;
        this.mDriverUid = mDriverUid;
        this.mOrigin = mOrigin;
        this.mDestination = mDestination;
        this.mDistance = mDistance;
        this.mPayment = mPayment;
        this.mTimeStart = mTimeStart;
        this.mTimeFinish = mTimeFinish;
        this.mStatus = mStatus;
        this.mKey = mKey;
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

    public float getmDistance() {
        return mDistance;
    }

    public void setmDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public float getmPayment() {
        return mPayment;
    }

    public void setmPayment(float mPayment) {
        this.mPayment = mPayment;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("mClientUid", mClientUid);
        result.put("mDriverUid", mDriverUid);
        result.put("mOrigin", mOrigin);
        result.put("mDestination", mDestination);
        result.put("mStatus", mStatus);
        result.put("mKey", mKey);

        return result;
    }
}
