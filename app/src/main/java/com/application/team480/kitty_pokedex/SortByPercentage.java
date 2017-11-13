package com.application.team480.kitty_pokedex;

import java.util.Comparator;

/**
 * Created by MingKie on 11/11/2017.
 */

public class SortByPercentage implements Comparator<Result> {
    @Override
    public int compare(Result result1, Result result2) {
        int value = 0;
        if (result2.getPercentage() > result1.getPercentage()) {
            value = 1;
        } else if (result2.getPercentage() < result1.getPercentage()) {
            value = -1;
        } else if (result2.getPercentage() == result1.getPercentage()) {
            value = 0;
        }
        return value;
    }
}
