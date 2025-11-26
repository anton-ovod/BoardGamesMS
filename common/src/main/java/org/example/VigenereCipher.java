package org.example;

public class VigenereCipher {
    private static final int BYTE_MASK = 0xFF;
    private final byte[] keyBytes;

    public VigenereCipher(String key) {
        this.keyBytes = key.getBytes();
    }

    public byte[] encrypt(byte[] data) {
        return processCipherOperation(data, true);
    }

    public byte[] decrypt(byte[] data) {
        return processCipherOperation(data, false);
    }

    private byte[] processCipherOperation(byte[] data, boolean isEncryption) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            int dataByte = data[i] & BYTE_MASK;
            int keyByte = keyBytes[i % keyBytes.length] & BYTE_MASK;
            int operation = isEncryption ? dataByte + keyByte : dataByte - keyByte;
            result[i] = (byte) (operation & BYTE_MASK);
        }
        return result;
    }
}
