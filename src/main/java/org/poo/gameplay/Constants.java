package org.poo.gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

}
