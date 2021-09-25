package runtime;

import businesslogic.ChatProvider;
import proxies.ChatProviderServerProxy;

import java.io.IOException;
import java.net.ServerSocket;

public class Runtime {
    public static void main(String[] args) {
        ChatProvider provider = new ChatProvider();

        try (ServerSocket serverSocket = new ServerSocket(9001)) {
            while (true) {
                new Thread(new ChatProviderServerProxy(serverSocket.accept(), provider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
