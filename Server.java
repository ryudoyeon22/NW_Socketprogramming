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
            listener = new ServerSocket(1234); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.....");

            while (true) {
                Socket socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
                System.out.println("연결되었습니다.");

                Runnable clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start(); // 각 클라이언트에 대한 처리를 위한 스레드 시작
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (listener != null)
                    listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("서버 소켓 닫는 중 오류가 발생했습니다.");
            }
        }
    }

    static class ClientHandler implements Runnable{
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
                        System.out.println("클라이언트에서 연결을 종료하였음");
                        break; // "bye"를 받거나 연결이 끊기면 종료
                    }
                    System.out.println(inputMessage); // 받은 메시지를 화면에 출력

                    String res = calc(inputMessage); // 계산. 계산 결과는 res
                    out.write(res + "\n"); // 계산 결과 문자열 전송
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("클라이언트와의 통신 중 오류가 발생했습니다.");
            } finally {
                try {
                    if (out != null)
                        out.close();
                    if (in != null)
                        in.close();
                    if (socket != null)
                        socket.close(); // 통신용 소켓 닫기
                } catch (IOException e) {
                    System.out.println("클라이언트와의 통신 소켓 닫기 중 오류가 발생했습니다.");
                }
            }
        }

        public static String calc(String exp) {
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
                    res = Integer.toString(op1 + op2);
                    break;
                case "SUB":
                    res = Integer.toString(op1 - op2);
                    break;
                case "MUL":
                    res = Integer.toString(op1 * op2);
                    break;
                case "DIV":
                    if (op2 != 0) {
                        res = Integer.toString(op1 / op2);
                    } else {
                        return "Incorrect: Divided by zero";
                    }
                    break;
                default:
                    res = "Incorrect: Invalid message";
            }
            return res;
        }
    }
}

