package org.example.modules.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.VigenereCipher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class TcpClientService {
    @Value("${tcp.server.host}")
    private String host;

    @Value("${tcp.server.port}")
    private int port;

    @Value("${cipher.key}")
    private String cipherKey;

    public byte[] requestFile(String fileName) throws Exception {

        try (Socket socket = new Socket(host, port);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {

            VigenereCipher cipher = new VigenereCipher(cipherKey);
            byte[] encryptedRequest = cipher.encrypt(fileName.getBytes());

            out.write(ByteBuffer.allocate(4).putInt(encryptedRequest.length).array());
            out.write(encryptedRequest);
            out.flush();

            byte[] lenBuf = in.readNBytes(4);
            int respLen = ByteBuffer.wrap(lenBuf).getInt();

            byte[] encryptedResponse = in.readNBytes(respLen);

            byte[] decrypted = cipher.decrypt(encryptedResponse);
            String responseText = new String(decrypted, StandardCharsets.UTF_8);

            if (responseText.startsWith("ERR:")) {
                if (responseText.equals("ERR:NO_FILE")) {
                    throw new FileNotFoundException("File not found on TCP server");
                }

                throw new IOException("Unknown error from TCP server: " + responseText);
            }

            return decrypted;
        }
    }
}
