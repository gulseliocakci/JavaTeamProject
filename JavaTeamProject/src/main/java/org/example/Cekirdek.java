package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Cekirdek {

    private String txtKelime;
    private String dosyaYolu;
    private int toplamKelimeSayisi;

    public Cekirdek(File dosyaYolu, String txtKelime) {
        this.dosyaYolu = dosyaYolu.getAbsolutePath();
        this.txtKelime = txtKelime;
    }

    public int getToplamKelimeSayisi() {
        return toplamKelimeSayisi;
    }

    public void cekirdeklereBolme() {
        int parcaBoyutu = 1024; // Parça boyutu (örneğin, 1 KB)

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "UTF-8"))) {
            char[] buffer = new char[parcaBoyutu]; // okunucak verileri tutuyor
            int bytesRead;
            int chunkNumber = 1;

            int cekirdekSayisi = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(cekirdekSayisi); // iş parçacığı havuzu
            List<Future<Integer>> futures = new ArrayList<>(); // Future nesnelerini tutacak liste

            while ((bytesRead = reader.read(buffer)) != -1) { // tüm metin bitene kadar okuyacak
                System.out.println("Parça " + chunkNumber + " gönderiliyor...");
                String chunkData = new String(buffer, 0, bytesRead);  // okunan verileri string haline getiriyor.

                Future<Integer> future = executorService.submit(new Worker(chunkData, txtKelime)); // Parçayı işlemek üzere çekirdeğe gönder
                futures.add(future); // Future nesnesini listeye ekle
                chunkNumber++;
            }

            // Future nesnelerini izleme ve sonuçları alabilme
            toplamKelimeSayisi = 0;
            for (Future<Integer> future : futures) { // tüm future nesneleri döngüye girer.
                try {
                    int count = future.get(); // Görevin sonucunu al
                    toplamKelimeSayisi += count;
                    System.out.println("Görev tamamlandı, bulunan kelime sayısı: " + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Toplam kelime sayısı: " + toplamKelimeSayisi);
            executorService.shutdown(); // Havuzu kapat
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Worker sınıfı: Callable<Integer> arayüzünü uygulayan iş parçacığı sınıfı
    private static class Worker implements Callable<Integer> { // Callable<Integer> olarak Worker tanımı
        private String data;
        private String word;

        public Worker(String data, String word) {
            this.data = data;
            this.word = word;
        }

        @Override
        public Integer call() {
            //kelime sayısını bul

            int count = countOccurrences(data, word);
            System.out.println("Gelen parça işlendi, bulunan  sayısı: " + count);
            return count;
        }

        private int countOccurrences(String data, String word) {
            String[] words = data.split("[\\s\\p{Punct}&&[^'-]]+"); // boşluk ve noktalama işaretlerine göre ayırır
            int count = 0;
            for (String w : words) { // tüm kelimeler üzerinde döngü başlatır.
                if (w.equalsIgnoreCase(word)) {
                    count++;
                }
            }
            return count;
        }
    }
}
