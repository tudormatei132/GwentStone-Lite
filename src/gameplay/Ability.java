package gameplay;

abstract class Ability {
    private boolean mustBeCastOnEnemies;
    private String name;
    private Minion minion;
    abstract void useAbility(Minion target);

    public Ability(boolean mustBeCastOnEnemies, String name, Minion minion) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
        this.name = name;
        this.minion = minion;
    }

    public Minion getMinion() {
        return minion;
    }

    public void setMinion(Minion minion) {
        this.minion = minion;
    }

    public boolean castOnEnemies() {
        return mustBeCastOnEnemies;
    }

    public void setMustBeCastOnEnemies(boolean mustBeCastOnEnemies) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
    }
}
