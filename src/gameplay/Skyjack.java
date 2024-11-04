package gameplay;

public class Skyjack extends Ability{



    public Skyjack(Minion m) {
        super(true, "Skyjack", m);
    }



    public void useAbility(Minion target) {
        int temp = getMinion().getHealth();
        getMinion().setHealth(target.getHealth());
        target.setHealth(temp);
    }
}
