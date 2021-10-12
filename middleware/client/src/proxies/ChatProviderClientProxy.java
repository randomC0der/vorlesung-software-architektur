package proxies;

import businesslogic.IChatProvider;
import businesslogic.IChatter;
import util.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatProviderClientProxy implements IChatProvider, Closeable {

    private final Socket clientSocket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private final Map<IChatter, Integer> chatterMap = new HashMap<>();

    public ChatProviderClientProxy(String ipAddress, int port) throws IOException {
        clientSocket = new Socket(ipAddress, port);

        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

        final String selection = reader.readLine();
    }

    @Override
    public void anmelden(IChatter chatter) {
        writer.println(1);
        try {
            marshalChatter(chatter);
            String output = reader.readLine();
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abmelden(IChatter chatter) {
        writer.println(4);
    }

    @Override
    public void sendMessage(IChatter sender, String nachricht) {

    }

    private void marshalChatter(IChatter chatter) throws IOException {
        String prompt = reader.readLine();
        System.out.println(prompt);

        if (chatterMap.containsKey(chatter)) {
            writer.println(chatterMap.get(chatter));
        } else {
            chatterMap.put(chatter, chatter.hashCode());
            writer.println(chatterMap.get(chatter));
            prompt = reader.readLine();
            System.out.println(prompt);
            writer.println(chatter.getName());

            ServerSocket serverSocket;
            for (int i = 1024; i < 65535; i++) {
                try {
                    Util.log(i);
                    serverSocket = new ServerSocket(i);
                    ServerSocket finalServerSocket = serverSocket;
                    new Thread(() -> {
                        try {
                            Socket socket = finalServerSocket.accept();
                            ChatterServerProxy scp = new ChatterServerProxy(socket, chatter);
                            scp.run(); // dont try this at home
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    Thread.sleep(1000);

                    writer.println(i);

                    i = Integer.MAX_VALUE - 1; // break the loop
                } catch (IOException | InterruptedException ex) {
                    // Address already in use
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        writer.println(4);
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
