package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class TcpServer {
    private final int port;
    private final VigenereCipher cipher;
    private final Path filesDir;

    public TcpServer(int port, String key, Path filesDir) {
        this.port = port;
        this.cipher = new VigenereCipher(key);
        this.filesDir = filesDir;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP server listening on port " + port);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client.getInetAddress());

                new Thread(() -> handleClient(client)).start();
            }
        }
    }

    private void handleClient(Socket client) {
        try (Socket socket = client;
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            byte[] lenBuf = in.readNBytes(4);
            if (lenBuf.length < 4) return;

            int reqLen = ByteBuffer.wrap(lenBuf).getInt();
            byte[] encryptedRequest = in.readNBytes(reqLen);

            byte[] decrypted = cipher.decrypt(encryptedRequest);
            String fileName = new String(decrypted);

            System.out.println("Requested file: " + fileName);

            Path filePath = filesDir.resolve(fileName).normalize();

            if (!filePath.startsWith(filesDir) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
                String err = "ERR:NO_FILE";
                byte[] enc = cipher.encrypt(err.getBytes());
                sendWithLength(out, enc);
                return;
            }

            byte[] fileBytes = Files.readAllBytes(filePath);

            byte[] encryptedResponse = cipher.encrypt(fileBytes);
            sendWithLength(out, encryptedResponse);

            System.out.println("Sent file " + fileName + " (" + fileBytes.length + " bytes)");

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    private void sendWithLength(OutputStream out, byte[] data) throws IOException {
        byte[] length = ByteBuffer.allocate(4).putInt(data.length).array();
        out.write(length);
        out.write(data);
        out.flush();
    }
}
