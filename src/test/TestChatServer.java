import client.ChatClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class TestChatServer {

    private static int port;

    @BeforeAll
    public static void start() throws InterruptedException, IOException {

        // Take an available port
        ServerSocket s = new ServerSocket(0);
        port = s.getLocalPort();
        s.close();

        Executors.newSingleThreadExecutor().submit(() -> new ChatServer(port).start());
        Thread.sleep(2000);
    }

    @Test
    public void givenClient1_whenServerResponds_thenCorrect() {
        ChatClient client = new ChatClient("localhost", port, System.out::println);
        client.startClient();
        client.sendMessage("first_message");
    }
}
