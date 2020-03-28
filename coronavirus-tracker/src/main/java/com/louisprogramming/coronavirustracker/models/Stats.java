package com.louisprogramming.coronavirustracker.models;

public class Stats {
    private String country;
    private String state;
    private int latestConfirmedCases;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLatestConfirmedCases() {
        return latestConfirmedCases;
    }

    public void setLatestConfirmedCases(int latestConfirmedCases) {
        this.latestConfirmedCases = latestConfirmedCases;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", latestConfirmedCases=" + latestConfirmedCases +
                '}';
    }
}
