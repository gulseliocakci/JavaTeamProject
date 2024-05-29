package org.example;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;

public class Server extends dosyaGonder {
    private static String[] serverIPs = {"192.168.43.237", "192.168.43.175", "192.168.43.77"}; // Sunucu IP adresleri
    private static int[] ports = {7755, 7755, 7756}; // Sunucu port numaraları

    public Server(File dosyaYolu, String arananKelime) {
        super(dosyaYolu, arananKelime);
    }

    @Override
    public void dosyaBol() {
        dosyayiIsle(); // Sunuculara dosya parçalarını gönderme işlemleri
    }

    @Override
    public void dosyayiIsle() {
        int serverSayisi = getSunucuSayisi() ;

        File secilenDosya = new File(String.valueOf(dosyaYolu));

        try {
            // Dosyanın içeriğini bir StringBuilder'a yükle
            BufferedReader reader = new BufferedReader(new FileReader(secilenDosya));
            StringBuilder content = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                content.append((char) ch);
            }
            reader.close();

            // Dosya içeriğini belirtilen sayıda parçaya böl
            String contentStr = content.toString();
            int[] splitIndices = parcaIndeksleriniBulma(contentStr, serverSayisi); // Bölünmüş indeksler

            // Parçaları ayrı ayrı sunuculara gönder ve sonuçları al
            int kelimeninGecmeSayisi = 0;
            for (int i = 0; i < serverSayisi; i++) {
                int start = (i == 0) ? 0 : splitIndices[i - 1];
                int end = (i == serverSayisi - 1) ? contentStr.length() : splitIndices[i];
                String part = contentStr.substring(start, end);
                System.out.println("Sunucuya gönderiliyor: IP = " + serverIPs[i] + ", Port = " + ports[i]);
                kelimeninGecmeSayisi += dosyaParcasiniGonder(part, arananKelime, serverIPs[i], ports[i], secilenDosya.getName());
                toplamKelimeSayisi = kelimeninGecmeSayisi;
            }
            System.out.println("Toplam kelime sayısı: " + toplamKelimeSayisi);

        } catch (IOException e) {
            System.err.println("I/O hatası: " + e.getMessage());
        }
    }

    private static int[] parcaIndeksleriniBulma(String content, int parcaSayisi) {
        int[] indeksler = new int[parcaSayisi - 1];
        int parcaBoyutu = content.length() / parcaSayisi;
        for (int i = 1; i < parcaSayisi; i++) {
            int mid = parcaBoyutu * i;
            while (mid < content.length() && !Character.isWhitespace(content.charAt(mid)) && content.charAt(mid) != '\n')  {
                mid++;
            }
            indeksler[i - 1] = mid;
        }
        return indeksler;
    }

    private static int dosyaParcasiniGonder(String content, String arananKelime, String serverIP, int port, String partFileName) throws IOException {
        try (Socket socket = new Socket(serverIP, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); //İstemciye bir veri göndermek istenildiğinde getOutputStream metodu kullanılır.
             DataInputStream dis = new DataInputStream(socket.getInputStream())) { // Socket sınıfında yer alan getInputStream metodu ile istemciden gelen veriler okunmuştur.

            byte[] contentBytes = content.getBytes();

            // Dosya bilgilerini ve aranacak kelimeyi gönder
            dos.writeUTF(partFileName);
            dos.writeLong(contentBytes.length);
            dos.writeUTF(arananKelime);
            dos.write(contentBytes);

            // Sunucudan arama sonuçlarını al
            int mevcutSayi = dis.readInt();
            System.out.println("Dosya parçası gönderildi: " + partFileName + ", Kelime sayısı: " + mevcutSayi);
            return mevcutSayi;
        } catch (IOException e) {
            System.err.println("Sunucuya bağlanılamadı: IP = " + serverIP + ", Port = " + port + ", Hata: " + e.getMessage());
            return 0;
        }
    }
}
