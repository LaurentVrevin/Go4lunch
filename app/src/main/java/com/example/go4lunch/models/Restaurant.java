package com.example.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.models.nearbysearch.Photo;
import com.example.go4lunch.models.nearbysearch.Result;

public class Restaurant implements Parcelable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String websiteUrl;
    private List<Photo> photoUrl;
    private double latitude;
    private double longitude;
    private String openingHours;
    private String closingHours;
    private float rating;
    @Nullable
    private Double distance;
    private int workmatesCount;
    private int likesCount;

    public Restaurant() {
        // constructeur sans argument requis pour Firestore
    }

    public Restaurant(Result result) {
        this.id = result.getPlaceId();
        this.name = result.getName();
        this.address = result.getVicinity();
        this.phone = result.getFormattedPhoneNumber();
        this.websiteUrl = result.getWebsite();
        this.photoUrl = result.getPhotos();
        this.latitude = result.getGeometry().getLocation().getLat();
        this.longitude = result.getGeometry().getLocation().getLng();
        if (result.getOpeningHours() != null) {
            this.openingHours = result.getOpeningHours().isOpenNow() ? "Ouvert" : "Fermé";
        }
        if (result.getOpeningHours() != null && result.getOpeningHours().getCloseTime() != null) {
            this.closingHours = result.getOpeningHours().getCloseTime();
        }
        this.rating = (float) result.getRating();
        this.distance=null;
    }


    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        websiteUrl = in.readString();
        photoUrl = in.createTypedArrayList(Photo.CREATOR);
        latitude = in.readDouble();
        longitude = in.readDouble();
        openingHours = in.readString();
        closingHours = in.readString();
        rating = in.readFloat();
        if (in.readByte() == 0) {
            distance = null;
        } else {
            distance = in.readDouble();
        }
        workmatesCount = in.readInt();
        likesCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(websiteUrl);
        dest.writeTypedList(photoUrl);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(openingHours);
        dest.writeString(closingHours);
        dest.writeFloat(rating);
        if (distance == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(distance);
        }
        dest.writeInt(workmatesCount);
        dest.writeInt(likesCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getPlaceId() {
        return id;
    }

    public void setPlaceId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setPhotoUrl(List<Photo> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setClosingHours(String closingHours) {
        this.closingHours = closingHours;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public List<String> getPhotoUrls() {
        List<String> photoUrls = new ArrayList<>();
        if (photoUrl != null && !photoUrl.isEmpty()) {
            for (Photo photo : photoUrl) {
                String photoReference = photo.getPhotoReference();
                String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + BuildConfig.MAPS_API_KEY;
                photoUrls.add(photoUrl);
            }
        }
        return photoUrls;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getClosingHours() {
        return closingHours;
    }

    public float getRating() {
        return rating;
    }

    public Double getDistance() {
        //retourne 0.0 si la distance est null
        return distance !=null ? distance : 0.0 ;
    }
    public void setDistance(@Nullable Double distance) {
        this.distance = distance;
    }
    public int getWorkmatesCount() {
        return workmatesCount;
    }

    public void setWorkmatesCount(int workmatesCount) {
        this.workmatesCount = workmatesCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    // Ajouter +1 au compteur quand un collègue a choisi ce restaurant
    public void incrementWorkmatesCount() {
        workmatesCount++;
    }

    // Soustraire -1 au compteur quand un collègue a déselectionné ce restaurant
    public void decrementWorkmatesCount() {
        if (workmatesCount > 0) {
            workmatesCount--;
        }
    }
    public void incrementlikesCount() {
        likesCount++;
    }

    // Soustraire -1 au compteur quand un collègue a dislike ce restaurant
    public void decrementlikesCount() {
        if (likesCount > 0) {
            likesCount--;
        }
    }


}
