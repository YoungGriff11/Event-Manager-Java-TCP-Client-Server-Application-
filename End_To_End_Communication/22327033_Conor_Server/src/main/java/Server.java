
/**
 *
 * @author Conor School
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private static ServerSocket ss;
    private static final int PORT = 1234;
    static List<String> synchronizedEvents = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        System.out.println("The server is open \n");
        //System.out.println(synchronizedEvents); to test if events were added to the Arraylist

        try {
            ss = new ServerSocket(PORT);
            // Add shutdown hook for graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(Server::stop));

            // Continuously listen for client connections
            while (true) {
                try {
                    // Accept a new client connection
                    Socket link = ss.accept();
                    
                    // Generate a unique ID for the client (using the remote address)
                    String clientID = link.getRemoteSocketAddress().toString();
                    
                    // Create an instance of ClientConnections
                    ClientConnections clientConnection = new ClientConnections(link, clientID);
                    
                    // Start ClientConnections in a new thread
                    new Thread(clientConnection).start();
                    
                    System.out.println("New client connected: " + clientID);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
    }

    private static void stop() {
        try {
            ss.close();
            System.out.println("Server shutdown completed.");
        } catch (IOException e) {
            System.out.println("Error during server shutdown: " + e.getMessage());
        }
    }
}
