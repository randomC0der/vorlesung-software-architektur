package proxies;

import businesslogic.IChatter;
import util.Util;

import java.io.*;
import java.net.Socket;

public class ChatterClientStub implements IChatter {

    private final String name;
    private final Socket socket;

    private final BufferedReader reader;
    private final PrintWriter writer;

    public ChatterClientStub(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        Util.log("Waiting for protocol...");
        String line = reader.readLine(); // protocol
        System.out.println(line);
    }

    @Override
    public void listen(String message) {
        Util.log("listening...");
        writer.println(1);
        writer.println(message);

        try {
            String response = reader.readLine();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
