package businesslogic;

import java.util.ArrayList;
import java.util.List;

public class ChatProvider implements IChatProvider {

    private List<IChatter> chatters = new ArrayList<>();

    @Override
    public void anmelden(IChatter chatter) {
        chatters.add(chatter);
        sendMessage(chatter, "joined the chat");
    }

    @Override
    public void abmelden(IChatter chatter) {
        chatters.remove(chatter);
        sendMessage(chatter, "left the chat");
    }

    @Override
    public void sendMessage(IChatter sender, String nachricht) {
        for (IChatter chatter : chatters) {
            chatter.listen((chatter.equals(sender) ? "Ich" : sender.getName()) + ": " + nachricht);
        }
    }
}
