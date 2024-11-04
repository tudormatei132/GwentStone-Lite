package gameplay;

public class Shapeshift extends Ability{



    public Shapeshift(Minion m) {
        super(true, "Shapeshift", m);
    }



    public void useAbility(Minion target) {
        int temp = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(temp);
    }
}
