package org.poo.cards;


import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


public class Card {
    @Getter @Setter
    private int mana;
    @Getter @Setter
    private int health;
    @Getter @Setter
    private int attackDamage;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private ArrayList<String> colors;
    @Getter @Setter
    private boolean isFrozen;
    @Getter @Setter
    private boolean front;
    private boolean hasAttacked;

    public Card(final int mana, final int health, final int attackDamage,
                final String description, final String name, final ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.name = name;
        this.colors = colors;
        this.hasAttacked = false;
        this.attackDamage = attackDamage;
    }


    /**
     *
     * @param damage
     */
    public void reduceHealth(final int damage) {
        this.health -= damage;
    }



    /**
     *
     * @param mapper
     * @return
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode on = mapper.createObjectNode();
        on.put("mana", mana);
        on.put("attackDamage", attackDamage);
        on.put("health", health);
        on.put("description", description);
        ArrayNode temp = mapper.createArrayNode();
        for (String s : colors) {
            temp.add(s);
        }
        on.put("colors", temp);
        on.put("name", name);
        return on;
    }

    /**
     *
     * @return
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }

    /**
     *
     * @param hasAttacked
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}
