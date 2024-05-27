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
import java.io.*;


public class Cekirdek implements Serializable {

    private String txtKelime;
    private String dosyaYolu;
    private int toplamKelimeSayisi;

    // Public no-argument constructor
    public Cekirdek() {
    }

    public Cekirdek(File dosyaYolu, String txtKelime) {
        this.dosyaYolu = dosyaYolu.getAbsolutePath();
        this.txtKelime = txtKelime;
    }

    public String getTxtKelime() {
        return txtKelime;
    }

    public void setTxtKelime(String txtKelime) {
        this.txtKelime = txtKelime;
    }

    public String getDosyaYolu() {
        return dosyaYolu;
    }

    public void setDosyaYolu(String dosyaYolu) {
        this.dosyaYolu = dosyaYolu;
    }

    public int getToplamKelimeSayisi() {
        return toplamKelimeSayisi;
    }

    public void setToplamKelimeSayisi(int toplamKelimeSayisi) {
        this.toplamKelimeSayisi = toplamKelimeSayisi;
    }

    public void cekirdeklereBolme() {
        int parcaBoyutu = 4096; // Parça boyutu (örneğin, 1 KB)

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "UTF-8"))) {
            char[] buffer = new char[parcaBoyutu];
            int bytesRead;
            int chunkNumber = 1;

            int cekirdekSayisi = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(cekirdekSayisi);
            List<Future<Integer>> futures = new ArrayList<>();

            while ((bytesRead = reader.read(buffer)) != -1) {
                System.out.println("Parça " + chunkNumber + " gönderiliyor...");
                String chunkData = new String(buffer, 0, bytesRead);

                Future<Integer> future = executorService.submit(new Worker(chunkData, txtKelime));
                futures.add(future);
                chunkNumber++;
            }

            toplamKelimeSayisi = 0;
            for (Future<Integer> future : futures) {
                try {
                    int count = future.get();
                    toplamKelimeSayisi += count;
                    System.out.println("Görev tamamlandı, bulunan kelime sayısı: " + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Toplam kelime sayısı: " + toplamKelimeSayisi);
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Worker sınıfı: Callable<Integer> arayüzünü uygulayan iş parçacığı sınıfı
    private static class Worker implements Callable<Integer> {
        private String data;
        private String word;

        public Worker(String data, String word) {
            this.data = data;
            this.word = word;
        }

        @Override
        public Integer call() {
            int count = countOccurrences(data, word);
            System.out.println("Gelen parça işlendi, bulunan kelime sayısı: " + count);
            return count;
        }

        private int countOccurrences(String data, String word) {
            int count = 0;
            int idx = 0;
            while ((idx = data.toLowerCase().indexOf(word.toLowerCase(), idx)) != -1) {
                count++;
                idx += word.length();
            }
            return count;
        }
    }
}
