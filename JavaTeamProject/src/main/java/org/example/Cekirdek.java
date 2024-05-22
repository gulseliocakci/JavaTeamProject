package org.example;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Cekirdek {

    private String txtKelime;
    private String filePath;
    private int totalCount;

    public Cekirdek(File filePath, String txtKelime) {
        this.filePath = filePath.getAbsolutePath();
        this.txtKelime = txtKelime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void cekirdeklereBolme() {
        int chunkSize = 4096;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            char[] buffer = new char[chunkSize];
            int bytesRead;
            int chunkNumber = 1;

            int cekirdekSayisi = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(cekirdekSayisi);

            List<Future<Integer>> futures = new ArrayList<>(); // Future nesnelerini tutacak liste

            while ((bytesRead = reader.read(buffer)) != -1) {
                System.out.println("Parça " + chunkNumber + " gönderiliyor...");
                String chunkData = new String(buffer, 0, bytesRead);
                Future<Integer> future = executorService.submit(new Worker(chunkData, txtKelime)); // Parçayı işlemek üzere çekirdeğe gönder
                futures.add(future); // Future nesnesini listeye ekle
                chunkNumber++;
            }
            // Future nesnelerini izleme ve sonuçları alabilme
            totalCount = 0;
            for (Future<Integer> future : futures) {
                try {
                    int count = future.get(); // Görevin sonucunu al
                    totalCount += count;
                    System.out.println("Görev tamamlandı, bulunan kelime sayısı: " + count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Toplam kelime sayısı: " + totalCount);
            executorService.shutdown(); // Havuzu kapat
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Worker implements Callable<Integer> { // Callable<Integer> olarak Worker tanımı
        private String data;
        private String word;

        public Worker(String data, String word) {
            this.data = data;
            this.word = word;
        }

        @Override
        public Integer call() {
           //kelime sayısı

            int count = countOccurrences(data, word);
            System.out.println("Gelen parça işlendi, bulunan  sayısı: " + count);
            return count;
        }

        private int countOccurrences(String data, String word) {
            String[] words = data.split("\\W+");
            int count = 0;
            for (String w : words) {
                if (w.equalsIgnoreCase(word)) {
                    count++;
                }
            }
            return count;
        }
    }

}
