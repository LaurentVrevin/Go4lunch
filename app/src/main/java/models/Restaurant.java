package models;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String websiteUrl;
    private String photoUrl;
    private double latitude;
    private double longitude;
    private String openingHours;
    private String closingHours;
    private float rating;

    public Restaurant(String id, String name, String address, String phone, String websiteUrl, String photoUrl,
                      double latitude, double longitude, String openingHours, String closingHours, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.photoUrl = photoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingHours = openingHours;
        this.closingHours = closingHours;
        this.rating = rating;
    }
    public Restaurant() {
        // constructeur sans argument requis pour Firestore
    }

    public String getId() {
        return id;
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

    public String getPhotoUrl() {
        return photoUrl;
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
}
