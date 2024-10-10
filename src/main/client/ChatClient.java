package client;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {

    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;

    private Socket socket;

    public ChatClient(String address, int port, Consumer<String> onMessageReceived) {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.onMessageReceived = onMessageReceived;
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getStackTrace());
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void startClient() {
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    onMessageReceived.accept(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

    }
}
