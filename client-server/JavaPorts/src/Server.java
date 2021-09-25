import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        log("Startup");

        try (ServerSocket serverSocket = new ServerSocket(9001)) {
            while (true) {
                new Thread(new ConnectionHandler(serverSocket.accept())).start();
            }
        } catch (IOException e) {
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
