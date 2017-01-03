package com.kerer.taxiapp.model;

/**
 * Created by ivan on 04.01.17.
 */

public class Driver extends Client {
    private Car mCar;

    public Driver() {
    }

    public Car getmCar() {
        return mCar;
    }

    public void setmCar(Car mCar) {
        this.mCar = mCar;
    }
}
