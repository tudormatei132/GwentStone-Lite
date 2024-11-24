package org.poo.gameplay;

import java.util.ArrayList;
import java.util.Arrays;


public final class Constants {
    private Constants() {
    }
    public static final ArrayList<String> FRONT_CARDS = new ArrayList<>(Arrays.asList("Goliath",
                                                        "Warden", "The Ripper", "Miraj"));
    public static final ArrayList<String> BACK_CARDS = new ArrayList<>(Arrays.asList("Sentinel",
                                                       "Berserker", "The Cursed One", "Disciple"));
    public static final ArrayList<Integer> PLAYER_1_ROWS = new ArrayList<>(Arrays.asList(2, 3));
    public static final int HERO_HEALTH = 30;
    public static final int ROWS_NO = 4;
    public static final int MAX_CARDS_ON_ROW = 5;
    public static final int NOT_ENOUGH_MANA_ERROR_CODE = -2;
    public static final int LAST_ROW = 3;
    public static final int MAX_MANA_GAIN = 10;
    public static final int GODS_PLAN_HEAL = -2;
}
