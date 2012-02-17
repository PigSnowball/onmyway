package com.porknbunny.onmyway;

/**
 * Created by IntelliJ IDEA.
 * User: pigsnowball
 * Date: 18/02/2012
 * Time: 08:39
 * To change this template use File | Settings | File Templates.
 */
public class Place {
    private float mLatitude, mLongitude;
    private String mIcon, mReference , mName, mVicinity;

    public Place(float mLatitude, float mLongitude, String mIcon, String mReference, String mName, String vicinity) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mIcon = mIcon;
        this.mReference = mReference;
        this.mName = mName;
        this.mVicinity = vicinity;
    }

    public Place(){

    }

    public float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getmReference() {
        return mReference;
    }

    public void setmReference(String mReference) {
        this.mReference = mReference;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        this.mVicinity = vicinity;
    }

    @Override
    public String toString() {
        return "Place{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mIcon='" + mIcon + '\'' +
                ", mReference='" + mReference + '\'' +
                ", mName='" + mName + '\'' +
                ", mVicinity='" + mVicinity + '\'' +
                '}';
    }
}
