import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatThread extends Thread {

    private final ChatClient client;
    private List<ChatClient> clientList;

    private final BufferedReader in;

    public ChatThread(ChatClient client, List<ChatClient> clientList) {
        this.client = client;
        this.clientList = clientList;

        try {
            in = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {

        try {
            broadcast(client.getName() + "님이 연결되었습니다.", false);

            String message = null;
            while ((message = in.readLine()) != null) {
                if (message.equals("/list")) {
                    for (ChatClient c : clientList) {
                        System.out.println(c.getName());
                    }
                    continue;
                }
                broadcast(client.getName() + ": " + message, true);
            }
        } catch (Exception e) {
            System.out.println("Connection lost with Client: " + client.getName());
        } finally { // Thread 연결 끊어짐
            try {
                in.close();
                broadcast(client.getName() + "님이 연결이 끊어졌습니다.", false);

            } catch (Exception ignored) {}

            clientList.remove(client);

            try {
                client.getSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private void broadcast(String message, boolean includeMe){

        List<ChatClient> listClient = new ArrayList<>(clientList);

        try {
            for(ChatClient c : listClient) {
                if(!includeMe) { //나를 포함하지 않고 broadcast
                    if(c == this.client) { // 나인 경우 건너뛰기
                        continue;
                    }
                }
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(c.getSocket().getOutputStream()));
                pw.println(message);
                pw.flush();
            }
        } catch (Exception ignored) {}
    }
}
