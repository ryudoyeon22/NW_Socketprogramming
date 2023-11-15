package socketHW1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverIP = "localhost"; // Default server IP
        int port = 1234; // Default port
        Socket socket = null;

        try {
            File configFile = new File("server_info.dat");
            if (configFile.exists()) {
                BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
                String line = fileReader.readLine(); // Read the first line in the file

                if (line != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        serverIP = parts[0].trim(); // Update server IP from file
                        port = Integer.parseInt(parts[1].trim()); // Update port from file
                    }
                }
                fileReader.close();
            } else {
                System.out.println("Config file not found. Using default settings.");
            }

            socket = new Socket(serverIP, port); // Connect to the server

            try (Scanner scanner = new Scanner(System.in);
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                System.out.println("write down the value ");
                String message = scanner.nextLine(); // Receive text input from the user

                writer.write(message + "\n");
                writer.flush(); // Send string to server

                // Receive results from the server and output
                String receivedMessage = reader.readLine();
                System.out.println("Answer: " + receivedMessage);

            } catch (IOException e) {
                System.out.println("Incorret: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close(); // close socket
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }
}


