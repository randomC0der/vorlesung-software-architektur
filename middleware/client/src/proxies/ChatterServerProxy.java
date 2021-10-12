package proxies;

import businesslogic.IChatter;
import util.Util;

import java.io.*;
import java.net.Socket;

public class ChatterServerProxy implements Runnable, Closeable {

    private Socket socket;
    private IChatter chatter;

    private final BufferedReader reader;
    private final PrintWriter writer;

    public ChatterServerProxy(Socket socket, IChatter chatter) throws IOException {

        this.socket = socket;
        this.chatter = chatter;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        writer.println("1: listen");
    }

    @Override
    public void run() {

        boolean runnning = true;

        while (runnning) {
            try {

                String line = reader.readLine();
                Util.log(line);
                Util.log("Received: " + line);
                int input = Integer.parseInt(line);

                switch (input) {
                    case 1 -> {
                        line = reader.readLine();
                        Util.log("Received: " + line);
                        chatter.listen(line);
                        Util.log("Sending ok");
                        writer.println("ok");
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + input);
                }

            } catch (Exception e) {
                e.printStackTrace();
                runnning = false;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
