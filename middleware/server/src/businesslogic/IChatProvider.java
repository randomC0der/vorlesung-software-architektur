package businesslogic;

public interface IChatProvider {
    void anmelden(IChatter chatter);
    void abmelden(IChatter chatter);
    void sendMessage(IChatter sender, String nachricht);
}
