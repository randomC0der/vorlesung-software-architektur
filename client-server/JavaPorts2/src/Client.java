import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        log("Startup");

        try (Socket clientSocket = new Socket("localhost", 9001);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            log(reader.readLine());
            writer.println(1);
            log(reader.readLine());
            writer.println(2);
            log(reader.readLine());
            writer.println(2);
            log(reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void log(Object msg) {
        System.out.println("In client: " + msg);
    }
}
