package com.bridge.androidtechnicaltest.model;

import com.bridge.androidtechnicaltest.db.Pupil;

import java.io.Serializable;

public class PupilDetailApi implements Serializable {

    private long pupilId;

    public long getPupilId() {
        return pupilId;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private String country;

    private String name;

    private String image;

    private double latitude;

    private double longitude;

    public PupilDetailApi(long pupilId, String country, String name, String image, double latitude, double longitude) {
        this.pupilId = pupilId;
        this.country = country;
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude= longitude;
    }

    public Pupil mapToPupil() {
        return new Pupil(
                pupilId,
                name,
                country,
                image,
                latitude,
                longitude
        );
    }
}
