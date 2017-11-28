package com.application.team480.kitty_pokedex;

/**
 * Created by luis_cortes on 11/25/17.
 */

public class Characteristic {
    private CharacteristicType type;
    private String scale;

    public Characteristic() {

    }

    public CharacteristicType getType() {
        return type;
    }

    public void setType(CharacteristicType type) {
        this.type = type;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
