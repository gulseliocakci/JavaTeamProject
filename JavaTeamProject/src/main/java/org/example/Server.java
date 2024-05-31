package org.example;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;

public class Server extends dosyaGonder {
    private static String[] serverIPs = {"192.168.43.237", "192.168.43.175", "192.168.43.77"}; // Sunucu IP adresleri
    private static int[] ports = {7756, 7755, 7756}; // Sunucu port numaraları

    public Server(File dosyaYolu, String arananKelime) {
        super(dosyaYolu, arananKelime);
    }

    @Override
    public void dosyayiIsle() {
        int ClientSayisi = getClientsayisi() ;

        File secilenDosya = new File(String.valueOf(dosyaYolu));

        try {
            // Dosyanın içeriğini bir StringBuilder'a yükle
            BufferedReader reader = new BufferedReader(new FileReader(secilenDosya));
            StringBuilder metin = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                metin.append((char) ch);
            }
            reader.close();

            // Dosya içeriğini belirtilen sayıda parçaya böl
            String metinStr = metin.toString();
            int[] splitIndices = parcaIndeksleriniBulma(metinStr, ClientSayisi); // Bölünmüş indeksler

            // Parçaları ayrı ayrı sunuculara gönder ve sonuçları al
            int kelimeninGecmeSayisi = 0;
            for (int i = 0; i < ClientSayisi; i++) {
                int start = (i == 0) ? 0 : splitIndices[i - 1];  //Ternary Operator
                int end = (i == ClientSayisi - 1) ? metinStr.length() : splitIndices[i];
                String part = metinStr.substring(start, end);
                System.out.println("Sunucuya gönderiliyor: IP = " + serverIPs[i] + ", Port = " + ports[i]);
                kelimeninGecmeSayisi += dosyaParcasiniGonder(part, arananKelime, serverIPs[i], ports[i], secilenDosya.getName());
                toplamKelimeSayisi = kelimeninGecmeSayisi;
            }
            System.out.println("Toplam kelime sayısı: " + toplamKelimeSayisi);

        } catch (IOException e) {
            System.err.println("I/O hatası: " + e.getMessage());
        }
    }

    private static int[] parcaIndeksleriniBulma(String metin, int parcaSayisi) {
        int[] indeksler = new int[parcaSayisi - 1];
        int parcaBoyutu = metin.length() / parcaSayisi;
        for (int i = 1; i < parcaSayisi; i++) {
            int sonindeks = parcaBoyutu * i;
            while (sonindeks < metin.length() && !Character.isWhitespace(metin.charAt(sonindeks)) && metin.charAt(sonindeks) != '\n')  {
                sonindeks++;
            }
            indeksler[i - 1] = sonindeks;  // her parçanın son indeksini diziye kaydeder.
        }
        return indeksler;
    }

    private static int dosyaParcasiniGonder(String metin, String arananKelime, String serverIP, int port, String partFileName) throws IOException {
        try (Socket socket = new Socket(serverIP, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream()); //İstemciye bir veri göndermek istenildiğinde getOutputStream metodu kullanılır.
             DataInputStream dis = new DataInputStream(socket.getInputStream())) { // Socket sınıfında yer alan getInputStream metodu ile istemciden gelen veriler okunmuştur.

            byte[] contentBytes = metin.getBytes();

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
