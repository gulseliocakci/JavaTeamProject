package org.example;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Server extends GUI{
    private static String txtKelime1;
    private static String dosyaYolu1;
    private static int toplamKelimeSayisi;

    public Server(File dosyaYolu, String txtKelime) {
        dosyaYolu1 = dosyaYolu.getAbsolutePath();
        txtKelime1 = txtKelime;
    }

    public int getToplamKelimeSayisi() {
        return toplamKelimeSayisi;
    }

    public static void sendFileToServers() {

        int numberOfServers = getNumberOfServers();


        String[] serverIPs = {"10.50.30.145","10.50.30.145","10.50.22.142"}; // Manuel olarak girilmiş IP adresleri
        int[] ports = {7755, 7755, 7755}; // Sunucu port numaraları


            File selectedFile = new File(dosyaYolu1);



            try {
                // Dosyanın içeriğini bir StringBuilder'a yükle
                BufferedReader reader = new BufferedReader(new FileReader((selectedFile)));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();

                // Dosya içeriğini belirtilen sayıda parçaya böl
                String contentStr = content.toString();
                int[] splitIndices = findSplitIndices(contentStr, numberOfServers);

                // Parçaları ayrı ayrı sunuculara gönder ve sonuçları al
                int totalOccurrences = 0;
                for (int i = 0; i < numberOfServers; i++) {
                    int start = (i == 0) ? 0 : splitIndices[i - 1];
                    int end = (i == numberOfServers - 1) ? contentStr.length() : splitIndices[i];
                    String part = contentStr.substring(start, end);
                    System.out.println("Sunucuya gönderiliyor: IP = " + serverIPs[i] + ", Port = " + ports[i]);
                    totalOccurrences += sendFilePart(part, txtKelime1, serverIPs[i], ports[i], selectedFile.getName());
                    toplamKelimeSayisi=totalOccurrences;
                }
                System.out.println("Toplam kelime sayısı: " + toplamKelimeSayisi);


            } catch (IOException e) {
                System.err.println("I/O hatası: " + e.getMessage());
            }

    }

    private static int[] findSplitIndices(String content, int numberOfParts) {
        int[] indices = new int[numberOfParts - 1];
        int partSize = content.length() / numberOfParts;
        for (int i = 1; i < numberOfParts; i++) {
            int mid = partSize * i;
            while (mid < content.length() && content.charAt(mid) != ' ' && content.charAt(mid) != '\n') {
                mid++;
            }
            indices[i - 1] = mid;
        }
        return indices;
    }

    private static int sendFilePart(String content, String txtKelime1, String serverIP, int port, String partFileName) throws IOException {
        try (Socket socket = new Socket(serverIP, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            byte[] contentBytes = content.getBytes();

            // Dosya bilgilerini ve aranacak kelimeyi gönder
            dos.writeUTF(partFileName);
            dos.writeLong(contentBytes.length);
            dos.writeUTF(txtKelime1);
            dos.write(contentBytes);

            // Sunucudan arama sonuçlarını al
            int occurrenceCount = dis.readInt();
            System.out.println("Dosya parçası gönderildi: " + partFileName + ", Kelime sayısı: " + occurrenceCount);
            return occurrenceCount;
        } catch (IOException e) {
            System.err.println("Sunucuya bağlanılamadı: IP = " + serverIP + ", Port = " + port + ", Hata: " + e.getMessage());
            return 0;
        }
    }
}
