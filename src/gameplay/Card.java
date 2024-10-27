package gameplay;

import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private String name;
    private ArrayList<String> colors;
    boolean isFrozen;
    private boolean front;

    public Card(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors, boolean frozen) {
        this.mana = mana;
        this.health = health;
        this.description = description;
        this.name = name;
        this.colors = colors;
        this.isFrozen = frozen;
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
}
