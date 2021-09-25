import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        log("Startup");

        try (Socket clientSocket = new Socket("172.30.35.85", 9001);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            log(reader.readLine()); // Hallo
            writer.println(1); // Addieren
            Thread.sleep(3000);
            log(reader.readLine()); // Aufforderung
            writer.println(2); // Die Zahl
            log(reader.readLine()); // Output Summe
            log(reader.readLine()); // Hallo
            Thread.sleep(10_000);
            writer.println(2); // Abmelden
            log(reader.readLine()); // Tschüss

            //writer.println(1);
            //log(reader.readLine());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(Object msg) {
        System.out.print (msg + " ");

        try {
            throw new Exception();
        } catch (Exception e) {
            System.out.println(e.getStackTrace()[e.getStackTrace().length - 1]);
        }
    }
}
