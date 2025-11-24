package org.example;

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
}
