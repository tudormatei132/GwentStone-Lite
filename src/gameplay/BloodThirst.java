package gameplay;

public class BloodThirst extends HeroAbility {
    public BloodThirst() {
        super(false);
    }

    public void useAbility(Row row) {
        for (Minion m : row.getRow()) {
            m.setAttackDamage(m.getAttackDamage() + 1);
        }
    }
}
