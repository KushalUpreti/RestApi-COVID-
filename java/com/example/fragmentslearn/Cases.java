package com.example.fragmentslearn;

import java.io.Serializable;

public class Cases implements Serializable {
    private final String countryName;
    private final int newConfirmed;
    private final int totalConfirmed;
    private final int totalDeaths;
    private final int newDeaths;
    private final int newRecovered;
    private final int totalRecovered;

    public Cases(String countryName, int newConfirmed, int totalConfirmed, int totalDeaths, int newDeaths, int newRecovered, int totalRecovered) {
        this.countryName = countryName;
        this.newConfirmed = newConfirmed;
        this.totalConfirmed = totalConfirmed;
        this.totalDeaths = totalDeaths;
        this.newDeaths = newDeaths;
        this.newRecovered = newRecovered;
        this.totalRecovered = totalRecovered;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }
}
