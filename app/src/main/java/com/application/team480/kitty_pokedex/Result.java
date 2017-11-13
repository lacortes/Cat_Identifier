package com.application.team480.kitty_pokedex;

import java.util.Comparator;

/**
 * Created by MingKie on 11/11/2017.
 */

public class Result {
    private String breed;
    private float percentage;

    public Result(String breed, float percentage) {
        this.breed = breed;
        this.percentage = percentage;
    }

    public String getBreed() {
        return breed;
    }

    public float getPercentage() {
        return percentage;
    }
}
