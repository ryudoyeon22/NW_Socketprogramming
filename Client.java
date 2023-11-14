package socketHW1;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverIP = "localhost"; // Default server IP
        int port = 1234; // Default port

        try {
            File configFile = new File("server_info.dat");
            if (configFile.exists()) {
                BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
                String line;
                while ((line = fileReader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        if (key.equalsIgnoreCase("serverIP")) {
                            serverIP = value;
                        } else if (key.equalsIgnoreCase("port")) {
                            port = Integer.parseInt(value);
                        }
                    }
                }
                fileReader.close();
            } else {
                System.out.println("Config file not found. Using default settings.");
            }

            Socket socket = new Socket(serverIP, port); // Connect to the server
            // Rest of the code remains the same as your previous implementation...
            // ...reading user input, sending data to the server, receiving and printing results.

            socket.close(); // Close the socket
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

