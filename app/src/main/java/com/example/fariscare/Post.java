package com.example.fariscare;

public class Post {

    private int infected;
    private int deceased;
    private int recovered;
    private String sourceUrl;
    private String lastUpdatedAtApify;
    private String readMe;
    private int discharged;
    private int inCommunityFacilities;
    private int stableHospitalized;
    private int activeCases;
    private int criticalHospitalized;

    public int getCriticalHospitalized() {
        return criticalHospitalized;
    }

    public int getDischarged() {
        return discharged;
    }

    public int getInCommunityFacilities() {
        return inCommunityFacilities;
    }

    public int getStableHospitalized() {
        return stableHospitalized;
    }

    public int getActiveCases() {
        return activeCases;
    }

    public int getInfected() {
        return infected;
    }

    public int getDeceased() {
        return deceased;
    }

    public int getRecovered() {
        return recovered;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getLastUpdatedAtApify() {
        return lastUpdatedAtApify;
    }

    public String getReadMe() {
        return readMe;
    }
}

