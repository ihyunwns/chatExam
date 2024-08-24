import java.io.*;
import java.net.Socket;

public class ChatService {

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your name: ");
        String name = in.readLine();

        Socket socket = new Socket("localhost", 9999); //Server Socket 접속

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true); // ChatServer 출력 스트림

        out.println(name); // name 소켓에 전달

        // 채팅 받아오는 쓰레드
        InputThread inputThread = new InputThread(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        inputThread.start();

        // 채팅 기능
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("/quit")) {
                    throw new Exception();
                }
                out.println(message);
            }
        } catch (Exception ignored) {}
        finally{
            System.out.println("[CHAT] 채팅을 종료합니다.");
            socket.close();
        }

    }
}

class InputThread extends Thread {

    // 소켓으로 받아온 메시지
    private BufferedReader in;

    public InputThread(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try{
            String message = null;
            while ( (message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (Exception ignored) {}
    }
}
