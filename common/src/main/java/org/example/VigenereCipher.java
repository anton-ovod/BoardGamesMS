package org.example;

import java.nio.charset.StandardCharsets;

public class VigenereCipher {
    private final byte[] keyBytes;

    public VigenereCipher(String key)
    {
        this.keyBytes = key.getBytes();
    }

    public byte[] encrypt(byte[] data)
    {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            int d = data[i] & 0xFF;
            int k = keyBytes[i % keyBytes.length] & 0xFF;
            out[i] = (byte) ((d + k) & 0xFF);
        }
        return out;
    }

    public byte[] decrypt(byte[] data) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            int d = data[i] & 0xFF;
            int k = keyBytes[i % keyBytes.length] & 0xFF;
            out[i] = (byte) ((d - k) & 0xFF);
        }
        return out;
    }

    public byte[] encrypt(String text) {
        return encrypt(text.getBytes(StandardCharsets.UTF_8));
    }

    public String decryptToString(byte[] data) {
        return new String(decrypt(data), StandardCharsets.UTF_8);
    }
}
