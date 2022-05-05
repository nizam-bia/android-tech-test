package com.bridge.androidtechnicaltest.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.bridge.androidtechnicaltest.model.PupilUi;

@Entity(tableName = "Pupils")
public class Pupil {
    @PrimaryKey
    @ColumnInfo(name = "pupil_id")
    private Long pupilId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "country")
    private String value;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "latitude")
    private Double latitude;

    @ColumnInfo(name = "longitude")
    private Double longitude;

    public Pupil(Long pupilId, String name, String value, String image, Double latitude, Double longitude) {
        this.pupilId = pupilId;
        this.name = name;
        this.value = value;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getPupilId() {
        return pupilId;
    }

    public void setPupilId(Long pupilId) {
        this.pupilId = pupilId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "pupilId=" + pupilId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", image='" + image + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public PupilUi toPupilUi() {
        return new PupilUi(
                pupilId,
                name,
                value
        );
    }
}