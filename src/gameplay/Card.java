package gameplay;

import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Card {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private String name;
    private ArrayList<String> colors;

    private boolean isFrozen;
    private boolean front, hasAttacked;
    public Card(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.name = name;
        this.colors = colors;
        this.hasAttacked = false;
        this.attackDamage = attackDamage;
    }

    public boolean isFront() {
        return this.front;
    }

    public void setFront(boolean front) {
        this.front = front;
    }

    public void reduceHealth(int damage) {
        this.health -= damage;
    }

    public int getMana() {
        return this.mana;
    }

    public ArrayList<String> getColors() {
        return this.colors;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getAttackDamage() {
        return this.attackDamage;
    }

    public int getHealth() {
        return this.health;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }

    public void setFrozen(boolean frozen) {
        this.isFrozen = frozen;
    }

    public ObjectNode print(ObjectMapper mapper) {
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


    public void attack(Card c) {
        c.reduceHealth(this.getAttackDamage());
    }
    public void resetAttack() {
        hasAttacked = false;
    }

}
