package businesslogic;

import util.Util;

public class Chatter implements IChatter {

    private final String name;

    public Chatter(String name) {
        this.name = name;
    }

    @Override
    public void listen(String message) {
        Util.log("Ich höre: " + message);
    }

    @Override
    public String getName() {
        return name;
    }
}
