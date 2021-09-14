import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        log("Startup");

        try (ServerSocket serverSocket = new ServerSocket(9001);
             Socket socket = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            log(1);
            while(true) {
                writer.println("Hallo, ich bin der Addier-Server?\n1 für addieren, 2 für Tschüss");
                log(2);

                if (1 == readInt(reader)) {
                    log(3);
                    writer.println("Bitte gebe die erste Zahl ein: ");
                    int a = readInt(reader);
                    writer.println("Bitte gebe die zweite Zahl ein: ");
                    int b = readInt(reader);
                    writer.println(add(a, b));
                }

                if(2 == readInt(reader)) {
                    System.exit(0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void log(Object msg) {
        System.out.println("Server: " + msg);
    }

    private static int add(int a, int b) {
        return a + b;
    }

    // Todo: error handling
    private static int readInt(BufferedReader reader) throws IOException {
        return Integer.parseInt(reader.readLine());
    }
}
