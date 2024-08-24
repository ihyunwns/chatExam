import java.net.Socket;

public class ChatClient {

    private final String name;
    private final Socket socket;

    public ChatClient(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }

}
