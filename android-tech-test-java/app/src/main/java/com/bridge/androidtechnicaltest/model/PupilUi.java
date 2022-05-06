package com.bridge.androidtechnicaltest.model;


import java.io.Serializable;

public class PupilUi implements Serializable {

    public PupilUi(long pupilId, String pupilName, String country) {
        this.mPupilId = pupilId;
        this.mPupilName = pupilName;
        this.mCountry = country;
    }

    private final long mPupilId;

    private final String mPupilName;

    private final String mCountry;

    public long getPupilId() {
        return mPupilId;
    }

    public String getPupilName() {
        return mPupilName;
    }

    public String getCountry() {
        return mCountry;
    }
}
