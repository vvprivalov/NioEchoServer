import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final Scanner scanner;

    public static void main(String[] args) {

        new Client();
    }

    public Client() {
        scanner = new Scanner(System.in);
        openConnection();
        sendMessage();
    }

    private void sendMessage() {
        while (true) {
            String msg = scanner.nextLine();
            try {
                outputStream.writeUTF(msg);
                if (msg.equals("quit")) {
                    scanner.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openConnection() {
        try {
            socket = new Socket("localhost", 9000);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String msgFromMessage = inputStream.readUTF();
                        if (msgFromMessage.equals("quit")) {
                            break;
                        }
                        System.out.println(msgFromMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}