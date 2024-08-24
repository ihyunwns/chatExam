import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {

    // 공유 객체에서 쓰레드에 안전한 리스트를 만든다.
    static List<ChatClient> clientList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        // 서버 소켓 생성
        ServerSocket serverSocket = new ServerSocket(9999);

        while (true) {
            // 접속 대기 하다가 클라이언트가 접속하면 Socket 제공
            System.out.println("Waiting for connection...");
            Socket socket = serverSocket.accept(); // 접속 대기하다가 접속되면 socket 생성

            // Service 클래스에서 생성된 Client
            ChatClient client = createChatClient(socket);
            //새로운 클라이언트 목록에 추가
            clientList.add(client);

            ChatThread chatThread = new ChatThread(client, clientList);
            chatThread.start();

            System.out.println("Connected Socket: " + client.getName());
        }
    }

    private static ChatClient createChatClient(Socket socket) throws IOException {

        // ChatService 에서 전달한 name 값을 받고 ChatClient 객체 생성 해줌
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String name = in.readLine();

        return new ChatClient(name, socket);
    }

}

