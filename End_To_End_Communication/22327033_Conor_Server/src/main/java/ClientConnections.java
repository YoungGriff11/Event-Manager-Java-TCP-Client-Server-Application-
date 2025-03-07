
/**
 *
 * @author Conor School
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnections implements Runnable {

    private Socket clientLink;
    private String clientID;

    // Constructor to initialize client connection with socket and client ID
    public ClientConnections(Socket connection, String clientID) {
        this.clientLink = connection;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientLink.getInputStream())); PrintWriter out = new PrintWriter(clientLink.getOutputStream(), true)) {

            String message;
            // Loop to handle multiple messages from the client
            while ((message = in.readLine()) != null) {

                // Skip any empty or blank messages to avoid processing them
                if (message.trim().isEmpty()) {
                    continue; // Skip empty messages, as i had a bug where it sent two messages from Client --> server. 1 with the action and 1 blank causing both responses to appear
                }

                System.out.println("Message received from " + clientID + ": " + message);
                String response;

                try {
                    // Process the message and return a response
                    response = process(message);
                } catch (IncorrectActionException e) {
                    response = e.getMessage(); // Handle invalid action exceptions
                }

                // Send the response back to the client
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Error in connection with " + clientID + ": " + e.getMessage());
        } finally {
            try {
                System.out.println("\n* Closing Connection with " + clientID + " *");
                clientLink.close(); // Close the client connection socket
            } catch (IOException e) {
                System.out.println("Cannot disconnect from " + clientID + "!");
            }
        }
    }

    // Method to process the message from client
    private String process(String message) throws IncorrectActionException {
        // Expected format: action; date; time; description
        String[] parts = message.split(";", 4);
        if (parts.length != 4) {
            throw new IncorrectActionException("Invalid message format. Expected: action; date; time; description");
        }

        String action = parts[0].trim();
        String dateTime = parts[1].trim() + " " + parts[2].trim();
        String description = parts[3].trim();

        // Check the action type (add or remove) and perform respective operations
        if (action.equalsIgnoreCase("add")) {
            Server.synchronizedEvents.add(dateTime + ", " + description);
            return "Event added: " + dateTime + ", " + description;
        } else if (action.equalsIgnoreCase("remove")) {
            boolean removed = Server.synchronizedEvents.remove(dateTime + ", " + description);
            return removed ? "Event removed: " + dateTime + ", " + description : "Event not found.";
        } else {
            throw new IncorrectActionException("Invalid action: " + action);
        }
    }

    // Exception class for handling incorrect actions
    public static class IncorrectActionException extends Exception {

        public IncorrectActionException(String message) {
            super(message);
        }
    }
}
