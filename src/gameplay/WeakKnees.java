package gameplay;

public class WeakKnees extends Ability {

    public WeakKnees(Minion m) {
        super(true, "Weak Knees", m);
    }



    public void useAbility(Minion target) {
        target.setAttackDamage(Math.max(target.getAttackDamage() - 2, 0));
    }
}
