package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Main {
    static void main() throws IOException {
        Properties props = new Properties();
        props.load(Files.newInputStream(Path.of("tcp-server/config.properties")));

        String key = props.getProperty("cipher.key");
        int port = Integer.parseInt(props.getProperty("server.port"));
        Path filesDir = Path.of(props.getProperty("files.directory"));

        TcpServer server = new TcpServer(port, key, filesDir);

        server.start();
    }
}
