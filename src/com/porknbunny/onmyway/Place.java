package com.porknbunny.onmyway;

import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

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
    private float mDistanceToUser, mDistanceToLatPoint;
    private Location mNearestLatitude;
    private String mNearestLatitudeNice;

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

    public float getmDistanceToLatPoint() {
        return mDistanceToLatPoint;
    }

    public void setmDistanceToLatPoint(float mDistanceToLatPoint) {
        this.mDistanceToLatPoint = mDistanceToLatPoint;
    }

    public Location getmNearestLatitude() {
        return mNearestLatitude;
    }

    public void setmNearestLatitude(Location mNearestLatitude) {
        this.mNearestLatitude = mNearestLatitude;
    }

    public String getmNearestLatitudeNice() {
        return mNearestLatitudeNice;
    }

    public void setmNearestLatitudeNice(String mNearestLatitudeNice) {
        this.mNearestLatitudeNice = mNearestLatitudeNice;
    }

    public Location getLocation(){
        Location location = new Location("Latitude");
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);
        return location;
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
