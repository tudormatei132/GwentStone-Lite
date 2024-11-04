package gameplay;

public class GodsPlan extends Ability{



    public GodsPlan(Minion m) {
        super(false, "God's Plan", m);
    }



    public void useAbility(Minion target) {
        target.reduceHealth(-2);
    }
}
