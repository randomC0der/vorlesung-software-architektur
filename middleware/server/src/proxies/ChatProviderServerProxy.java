package proxies;

import businesslogic.ChatProvider;
import businesslogic.IChatter;
import util.Util;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ChatProviderServerProxy implements Runnable {

    // region DummyChatter
    private class DummyChatter implements IChatter {

        private String name;

        public DummyChatter(String name) {
            this.name = name;
        }

        @Override
        public void listen(String message) {
            Util.log(message);
        }

        @Override
        public String getName() {
            return name;
        }
    }
    // endregion

    private Socket socket;
    private ChatProvider provider;
    private BufferedReader reader;
    private PrintWriter writer;
    private HashMap<Integer, IChatter> chatterMap = new HashMap<>();

    public ChatProviderServerProxy(Socket socket, ChatProvider provider) throws IOException {
        this.socket = socket;
        this.provider = provider;

        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public void run() {
        try (Socket socket = this.socket;
             PrintWriter writer = this.writer;
             BufferedReader reader = this.reader) {

            boolean connected = true;

            while (connected) {

                Util.log("Sending prompt");
                writer.println("1: Anmelden, 2: Abmelden, 3: Nachricht senden, 4: Socket schließen");
                int selection = readInt(reader);
                Util.log("Received: " + selection);

                switch (selection) {
                    case 1:
                        anmelden();
                        break;
                    case 2:
                        abmelden();
                        break;
                    case 3:
                        nachrichtSenden();
                        break;
                    case 4:
                        connected = false;
                        break;
                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IChatter unmarshalChatter() throws IOException {
        Util.log("Unmarshalling chatter");
        writer.println("ChatterId?: ");
        Util.log("Waiting for response...");
        int id = readInt(reader);
        Util.log("Received: " + id);

        if (chatterMap.containsKey(id)) {
            return chatterMap.get(id);
        }

        Util.log("Asking for name");
        writer.println("Name?");
        Util.log("Waiting for response...");

        String userId = reader.readLine();
        Util.log("Received userId: " + userId);
        int port = Integer.parseInt(reader.readLine());
        Util.log("Received port: " + port);
        IChatter chatter = new ChatterClientStub(userId, new Socket(socket.getInetAddress(), port));
        chatterMap.put(id, chatter);
        return chatter;
    }

    private void anmelden() throws IOException {
        provider.anmelden(unmarshalChatter());
        writer.println("ok");
    }

    private void abmelden() throws IOException {
        provider.abmelden(unmarshalChatter());
        writer.println("ok");
    }

    private void nachrichtSenden() throws IOException {

        String nachricht = "<Placeholder Nachricht>";

        provider.sendMessage(unmarshalChatter(), nachricht);
    }

    private int readInt(BufferedReader reader) throws IOException {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            Util.log(socket.getInetAddress().getHostName());
            return 0;
        }
    }
}
