package gameplay;

import java.util.ArrayList;

public class SubZero extends HeroAbility{


    public SubZero() {
        super(true);
    }

    public void useAbility(Row row) {
        for (Minion m : row.getRow()) {
            m.setFrozen(true);
        }
    }

}
