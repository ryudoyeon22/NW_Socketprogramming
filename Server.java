// Server.java
package socketHW1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server {
    public static void main(String[] args) {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(1234); // Create server socket
            System.out.println("Waiting for connection...");

            while (true) {
                Socket socket = listener.accept(); // Waiting for connection requests from clients
                System.out.println("Connected");

                Runnable clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start(); // Initiate threads for processing for each client
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (listener != null)
                    listener.close(); // close server socket
            } catch (IOException e) {
                System.out.println("Error closing server socket.");
            }
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            BufferedWriter out = null;

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (true) {
                    String inputMessage = in.readLine();
                    if (inputMessage == null || inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("Client terminated the connection");
                        break; // Terminate when you get "bye" or disconnect
                    }
                    //System.out.println(inputMessage); 

                    String res = calc(inputMessage); // res = calc's result
                    out.write(res + "\n"); // 계산 결과 문자열 전송 
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while communicating with the client.");
            } finally {
                try {
                    if (out != null)
                        out.close();
                    if (in != null)
                        in.close();
                    if (socket != null)
                        socket.close(); // close socket
                } catch (IOException e) {
                    System.out.println("Error closing communication socket with client.");
                }
            }
        }

        public static String calc(String exp) {
        	BasicCalculator calculator = new BasicCalculator();
        	
            StringTokenizer st = new StringTokenizer(exp, " ");
            if (st.countTokens() > 3)
                return "Incorrect: Too many arguments";
            else if (st.countTokens() < 3)
                return "Incorrect: Too little arguments";

            String res = "";
            String opcode = st.nextToken();
            int op1 = Integer.parseInt(st.nextToken());
            int op2 = Integer.parseInt(st.nextToken());
            switch (opcode) {
                case "ADD":
                	res = Integer.toString(calculator.ADD(op1, op2));
                	System.out.println(op1 + " + " + op2 + " = " + res);
                    break;
                case "SUB":
                    res = Integer.toString(calculator.SUB(op1, op2));
                    System.out.println(op1 + " - " + op2 + " = " + res);
                    break;
                case "MUL":
                    res = Integer.toString(calculator.MUL(op1, op2));
                    System.out.println(op1 + " * " + op2 + " = " + res);
                    break;
                case "DIV":
                        res = Integer.toString(calculator.DIV(op1, op2));

                    break;
                default:
                    res = "Incorrect: Invalid message";
            }
            return res;
        }
    }
}


