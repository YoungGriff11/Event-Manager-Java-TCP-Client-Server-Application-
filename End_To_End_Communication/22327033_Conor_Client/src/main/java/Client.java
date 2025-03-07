
/**
 *
 * @author Conor School
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private static InetAddress h; // To hold the host address
    private static final int PORT = 1234; // Port number for the server

    public static void main(String[] args) {
        try {
            // Get the local host address
            h = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("Host ID not found!");
            System.exit(1); // Exit if the host ID is not found
        }

        run(); // Start the main client process
    }

    private static void run() {
        Socket link = null; // Initialize the socket connection

        try {
            // Create a socket connection to the server
            link = new Socket(h, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);
            BufferedReader userEntry = new BufferedReader(new InputStreamReader(System.in));

            // Inform the user about the expected command format
            System.out.println("Welcome to the Event Manager!");
            System.out.println("Please use the following format for commands:");
            System.out.println("Add an event: add; date; time; description");
            System.out.println("Remove an event: remove; date; time; description");
            System.out.println("Type 'STOP' to end the session.");

            // Start a thread to read server responses
            new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        // Print server responses
                        System.out.println("\nSERVER RESPONSE> " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace(); // Print stack trace on error
                }
            }).start();

            String message; // Variable to hold user input
            // Loop for user input
            while (true) {
                System.out.print("Enter message to be sent to server: ");
                message = userEntry.readLine().trim(); // Read user input and trim whitespace

                 if(message.equalsIgnoreCase("stop")) { // Check for stop command
                    out.println(message); // Send STOP message to server
                    break; // Exit the loop
                }

                // Sending the users message to the server
                out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace on error
        } finally {
            try {
                if (link != null) {
                    System.out.println("\n* Closing connection... *");
                    link.close(); // Close the socket connection
                }
            } catch (IOException e) {
                System.out.println("Unable to disconnect/close!"); // Error closing connection
                System.exit(1); // Exit on failure to close connection
            }
        }
    }
}
