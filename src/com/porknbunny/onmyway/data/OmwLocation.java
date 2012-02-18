package com.porknbunny.onmyway.data;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 19/02/2012
 * Time: 00:18
 * To change this template use File | Settings | File Templates.
 */
public class OmwLocation {
    private double mLatitude, mLongitude;
    private long mSecondsSinceEpoch;

    public OmwLocation(double mLatitude, double mLongitude, long mSecondsSinceEpoch) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mSecondsSinceEpoch = mSecondsSinceEpoch;
    }

    @Override
    public String toString() {
        return "OmwLocation{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mSecondsSinceEpoch=" + mSecondsSinceEpoch +
                '}';
    }
}
