package berm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerSocketExample {
    private ServerSocket server;
    public static String Gateway = "192.168.1.33";
    private int port = 1470;

    public ServerSocketExample(String ip) {
        try {
            server = new ServerSocket(port);
            if (ip != null) {
              Gateway = ip;
            }
            System.out.println("Forward to: " + Gateway);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleConnection() {
        System.out.println("Waiting for user message...");

        while (true) {
            try {
                Socket socket = server.accept();
                new ConnectionHandler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Port Forwarder");
        ServerSocketExample ss;
        if (args.length > 0) {
          ss = new ServerSocketExample(args[0]);
        } else {
          ss = new ServerSocketExample(null);
        }
        ss.handleConnection();
    }
}

class ConnectionHandler implements Runnable {
    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;

        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try
        {
            OutputStream oos = socket.getOutputStream();
            InputStream ois = socket.getInputStream();

            while (ois.available() == 0);
            int count = ois.available();
            int i = 0;
            byte[] message = new byte[count];

            do {
              message[i++] = (byte) ois.read();
            } while (count > i);
            System.out.println(this.socket.getRemoteSocketAddress().toString() + ": " + new String(message));
            System.out.println("byte size: " + message.length);

            byte[] answer = askGateway(message);
            oos.write(answer);

            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] askGateway(final byte[] message) {
        try {
            Socket socket = new Socket(ServerSocketExample.Gateway, 1470);
            InputStream ois = socket.getInputStream();
            OutputStream oos = socket.getOutputStream();

            oos.write(message);
            printHex(message);

            while (ois.available() == 0);
            int count = ois.available();
            int i = 0;
            byte[] answer = new byte[count];

            do {
              answer[i++] += ois.read();
            } while (count > i);
            System.out.println("GW " + ServerSocketExample.Gateway + " said: " + new String(answer));
            System.out.println("byte size: " + answer.length);
            printHex(answer);

            ois.close();
            oos.close();
            socket.close();

            return answer;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void printHex(final byte[] message) {
        System.out.print("Hex: ");

        for (int i=0; i<message.length; i++) {
            String data = Integer.toHexString((int) message[i]) + " ";
            System.out.print(data);
        }
        System.out.println();
    }
}

