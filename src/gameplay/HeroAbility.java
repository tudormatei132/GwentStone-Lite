package gameplay;

abstract class HeroAbility {
    private boolean mustCastOnEnemies;
    abstract void useAbility(Row row);


    public boolean isMustCastOnEnemies() {
        return mustCastOnEnemies;
    }

    public HeroAbility(boolean mustCastOnEnemies) {
        this.mustCastOnEnemies = mustCastOnEnemies;
    }
}
