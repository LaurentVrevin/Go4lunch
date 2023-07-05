package com.example.go4lunch.models.nearbysearch;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private boolean openNow;
    private String openTime;
    private String closeTime;

    public boolean isOpenNow() {
        return openNow;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
