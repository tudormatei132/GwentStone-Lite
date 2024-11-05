package gameplay;

public class EarthBorn extends HeroAbility {

    public EarthBorn() {
        super(false);
    }

    public void useAbility(Row row) {
        for (Minion m : row.getRow()) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
