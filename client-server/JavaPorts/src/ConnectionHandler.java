import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ConnectionHandler implements Runnable {

    private Socket _socket;

    private static int sum = 0;

    public ConnectionHandler(Socket socket) {
        _socket = socket;
    }

    @Override
    public void run() {
        try (Socket socket = _socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(_socket.getOutputStream()), true)) {

            log(_socket.getInetAddress().getHostName() + " connected");
            log(1);

            boolean connected = true;

            while (connected) {
                log("in loop");
                writer.println("Hallo, ich bin der Addier-Server? 1 für addieren, 2 für Tschüss");

                int selection = readInt(reader);
                log("selection: " + selection);

                if (1 == selection) {
                    log(3);
                    writer.println("Bitte gebe eine Zahl ein: ");
                    int a = readInt(reader);
                    add(a);
                    writer.println(sum);

                } else if (2 == selection) {
                    writer.println("Tschüss");
                    connected = false;
                }
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
            System.out.println(e.getStackTrace()[e.getStackTrace().length - 2]);
        }
    }

    private static int actualSum = 0;

    private synchronized static void add(int n) {
        actualSum += n;
        log("actual sum: " + actualSum + " (" + Thread.currentThread().getId() + ")");

        try {
            Thread.sleep(new Random().nextInt(10_000));
            sum += n;
            log("sum: " + sum + " (" + Thread.currentThread().getId() + ")");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int readInt(BufferedReader reader) throws IOException {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            log(_socket.getInetAddress().getHostName());
            return 0;
        }
    }
}
