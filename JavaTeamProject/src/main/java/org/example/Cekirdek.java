package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Cekirdek extends dosyaGonder implements Serializable {




    public Cekirdek(File dosyaYolu, String arananKelime) {
        super(dosyaYolu, arananKelime);
    }

    public int getToplamKelimeSayisi() {
        return toplamKelimeSayisi;
    }

    @Override
    public void dosyaBol() {
        cekirdeklereBolme();

    }

    public void setToplamKelimeSayisi(int toplamKelimeSayisi) {
        this.toplamKelimeSayisi = toplamKelimeSayisi;
    }

    public void cekirdeklereBolme() {
        int parcaBoyutu = 8192; // Parça boyutu (örneğin, 1 KB)

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "UTF-8"))) {
            char[] buffer = new char[parcaBoyutu];
            int byteOkuma;
            int parcaNumarasi = 1;

            int cekirdekSayisi = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(cekirdekSayisi);
            List<Future<Integer>> futures = new ArrayList<>();

            while ((byteOkuma = reader.read(buffer)) != -1) {
                System.out.println("Parça " + parcaNumarasi + " gönderiliyor...");
                String chunkData = new String(buffer, 0, byteOkuma);

                Future<Integer> future = executorService.submit(new Worker(chunkData, arananKelime));
                futures.add(future);
                parcaNumarasi++;
            }

            toplamKelimeSayisi = 0;
            for (Future<Integer> future : futures) {
                try {
                    int count = future.get();
                    toplamKelimeSayisi += count;

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
            int count = sayimIslemi(data, word);
            System.out.println("Gelen parça işlendi, bulunan kelime sayısı: " + count);
            return count;
        }

        private int sayimIslemi(String data, String word) {
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
