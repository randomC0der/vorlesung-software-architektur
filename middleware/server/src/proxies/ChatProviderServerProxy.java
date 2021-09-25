package proxies;

import businesslogic.ChatProvider;
import businesslogic.IChatter;
import util.Util;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class ChatProviderServerProxy implements Runnable {

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

                writer.println("1: Anmelden, 2: Abmelden, 3: Nachricht senden, 4: Socket schließen");
                int selection = readInt(reader);

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
        writer.println("ChatterId?: ");
        int id = readInt(reader);

        if (chatterMap.containsKey(id)) {
            return chatterMap.get(id);
        }

        writer.println("Name?");

        IChatter chatter = new DummyChatter(reader.readLine());
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
