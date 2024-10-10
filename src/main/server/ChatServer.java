package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    private final List<ClientHandler> clients;
    private ServerSocket serverSocket;

    private final int port;

    public ChatServer(int port) {
        clients = new ArrayList<>();
        this.port = port;
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(3000);
        chatServer.start();
        System.out.println("Server is starting...");
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket, clients);
                clients.add(client);
                new Thread(client).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

}

class ClientHandler implements Runnable {

    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    private final List<ClientHandler> clients;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clients = clients;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null)

                for (ClientHandler client : clients) {
                    client.out.println(message);
                }

        } catch (IOException e) {
            System.out.println("An error occurred :" + e.getMessage());
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
