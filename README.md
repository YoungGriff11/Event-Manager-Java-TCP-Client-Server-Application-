# Event-Manager-Java-TCP-Client-Server-Application-
Overview
This project is a Java-based TCP client-server application that allows multiple clients to manage events over a network. The server handles multiple client connections and maintains a synchronized event list, while clients can send commands to add or remove events.

Features
Multithreaded Server – Supports multiple clients simultaneously.
Event Management – Clients can add or remove scheduled events.
Synchronized Event List – Ensures consistency in event storage.
Graceful Server Shutdown – Uses a shutdown hook to close connections properly.
Custom Exception Handling – Ensures correct command formats.

How It Works
Run the server: Start Server.java to begin listening for client connections.
Run the client: Start Client.java to connect to the server.
Use the following commands in the client:
Add an event: add;YYYY-MM-DD;HH:MM;Event Description
Remove an event: remove;YYYY-MM-DD;HH:MM;Event Description
To exit the client, type: STOP

Requirements
Java 8 or later
