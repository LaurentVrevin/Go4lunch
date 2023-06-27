package com.example.go4lunch.models.nearbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// "NearbySearchResponse" sera utilisée pour représenter la réponse de la recherche à proximité dans l'API Google Places
// Elle comprend toutes les infos retrounées par l'api via la requête
public class NearbySearchResponse {
    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<>();

    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;

    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<>();

    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
