/*package org.example;

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

                    String parcaDosyaAdi = dis.readUTF();
                    long dosyaBoyutu = dis.readLong();
                    String aramaTerimi = dis.readUTF();

                    byte[] buffer = new byte[4096];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int okunan;
                    long toplamOkunan = 0;
                    long kalan = dosyaBoyutu;

                    while ((okunan = dis.read(buffer, 0, Math.min(buffer.length, (int) kalan))) > 0) {
                        toplamOkunan += okunan;
                        kalan -= okunan;
                        baos.write(buffer, 0, okunan);
                    }

                    // Gelen dosyayı masaüstüne kaydet
                    String masaustuYolu = System.getProperty("user.home") + "/Desktop/";
                    File gonderilenDosya = new File(masaustuYolu + parcaDosyaAdi);
                    try (FileOutputStream fos = new FileOutputStream(gonderilenDosya)) {
                        fos.write(baos.toByteArray());
                    }

                    String icerik = new String(baos.toByteArray());
                    int gecmeSayisi = kelimeGecmeHesaplama(icerik, aramaTerimi);

                    // Sonuçları istemciye gönder
                    dos.writeInt(gecmeSayisi);
                    System.out.println("Arama tamamlandı: " + parcaDosyaAdi + " için " + gecmeSayisi + " sonuç bulundu.");
                } catch (IOException e) {
                    System.out.println("IO Hatası: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Port Hatası: " + e.getMessage());
        }
    }

    private static int kelimeGecmeHesaplama(String content, String searchTerm) {
        int count = 0;
        int index = 0;
        while ((index = content.indexOf(searchTerm, index)) != -1) {
            count++;
            index += searchTerm.length();
        }
        return count;
    }
}*/