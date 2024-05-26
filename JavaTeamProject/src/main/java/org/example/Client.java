/* package org.example;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        int port = 7755; // Sunucu port numarası

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SERVER " + port + " BAĞLANTI İÇİN HAZIR...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("CLIENT BAĞLANDI.");

                    // Gelen dosya bilgilerini ve aranacak kelimeyi al
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                    String partFileName = dis.readUTF();
                    long fileSize = dis.readLong();
                    String searchTerm = dis.readUTF();

                    byte[] buffer = new byte[4096];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int read;
                    long totalRead = 0;
                    long remaining = fileSize;

                    while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                        totalRead += read;
                        remaining -= read;
                        baos.write(buffer, 0, read);
                    }

                    // Gelen dosyayı masaüstüne kaydet
                    String desktopPath = System.getProperty("user.home") + "/Desktop/";
                    File outputFile = new File(desktopPath + partFileName);
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        fos.write(baos.toByteArray());
                    }

                    String content = new String(baos.toByteArray());
                    int occurrenceCount = countOccurrences(content, searchTerm);

                    // Sonuçları istemciye gönder
                    dos.writeInt(occurrenceCount);
                    System.out.println("Arama tamamlandı: " + partFileName + " için " + occurrenceCount + " sonuç bulundu.");
                } catch (IOException e) {
                    System.out.println("IO Hatası: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Port Hatası: " + e.getMessage());
        }
    }

    private static int countOccurrences(String content, String searchTerm) {
        int count = 0;
        int index = 0;
        while ((index = content.indexOf(searchTerm, index)) != -1) {
            count++;
            index += searchTerm.length();
        }
        return count;
    }
}*/