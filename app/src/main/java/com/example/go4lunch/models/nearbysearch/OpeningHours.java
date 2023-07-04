package com.example.go4lunch.models.nearbysearch;

import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    private boolean openNow;
    private String closeTime;


    public boolean isOpenNow() {
        return openNow;
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

    public String getOpeningHours(Result result) {
        OpeningHours openingHours = result.getOpeningHours();
        if (openingHours != null && openingHours.isOpenNow()) {
            // Le lieu est ouvert actuellement
            return "" + openingHours;
        } else {
            // Le lieu est fermé actuellement
            return "";
        }
    }

    public String getClosingHours(Result result) {
        OpeningHours openingHours = result.getOpeningHours();
        if (openingHours != null && openingHours.isOpenNow()) {
            // Le lieu est ouvert actuellement, il n'y a pas d'heure de fermeture spécifique
            return "Pas d'heure de fermeture spécifique";
        } else if (openingHours != null && openingHours.getCloseTime() != null) {
            // Le lieu est fermé actuellement et il y a une heure de fermeture spécifique
            return openingHours.getCloseTime();
        } else {
            // Les informations sur les heures de fermeture ne sont pas disponibles
            return "Inconnu";
        }
    }
}
