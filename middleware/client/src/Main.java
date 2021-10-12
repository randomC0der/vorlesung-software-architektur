import businesslogic.Chatter;
import businesslogic.IChatter;
import proxies.ChatProviderClientProxy;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {

            IChatter chatter = new Chatter("Foo");
            ChatProviderClientProxy cpcp = new ChatProviderClientProxy("localhost", 9001);
            cpcp.anmelden(chatter);
            cpcp.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
