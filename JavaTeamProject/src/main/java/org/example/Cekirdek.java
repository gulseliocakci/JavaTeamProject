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
    public void dosyayiIsle() {
        cekirdeklereBolme();
    }


    public void cekirdeklereBolme() {
        int parcaBoyutu = 8192;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dosyaYolu), "UTF-8"))) {
            //Bu yapı, dosyayı UTF-8 karakter kodlamasıyla okumak için bir zincirleme (chained) yapı oluşturur:
            // önce FileInputStream ile dosya okunur, sonra InputStreamReader ile karakterlere dönüştürülür, en son da BufferedReader ile tamponlanır.
            char[] buffer = new char[parcaBoyutu];
            int byteOkuma;
            int parcaNumarasi = 1;

            int cekirdekSayisi = Runtime.getRuntime().availableProcessors();

            ExecutorService executorService = Executors.newFixedThreadPool(cekirdekSayisi);
            List<Future<Integer>> futures = new ArrayList<>();
            //Future nesneleri, çoklu iş parçacıklarının (thread'lerin) çalışmasını temsil eden nesnelerdir
            // ve iş parçacıklarının geri dönüş değerlerini almayı sağlar.
            String kalanVeri = "";

            while ((byteOkuma = reader.read(buffer)) != -1) {
                System.out.println("Parça " + parcaNumarasi + " gönderiliyor...");
                String parcaVeri = kalanVeri + new String(buffer, 0, byteOkuma);

                // Parçanın sonundaki son boşluk karakterini bul
                int lastSpaceIdx = parcaVeri.lastIndexOf(' ');
                if (lastSpaceIdx == -1) {
                    lastSpaceIdx = parcaVeri.length(); // Boşluk yoksa tüm parça işlenir
                }

                String currentPart = parcaVeri.substring(0, lastSpaceIdx);
                kalanVeri = parcaVeri.substring(lastSpaceIdx);

                Future<Integer> future = executorService.submit(new Worker(currentPart, arananKelime)); //submit yöntemi, iş parçacığını thread havuzuna gönderir ve bir Future nesnesi döndürür.
                futures.add(future);                                                                    //Bu nesne, iş parçacığının geri dönüş değerini temsil eder.
                parcaNumarasi++;
            }

            // Son kalan kısmı da işleme ekle
            if (!kalanVeri.isEmpty()) {
                Future<Integer> future = executorService.submit(new Worker(kalanVeri, arananKelime));
                futures.add(future);
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
            executorService.shutdown(); //iş parçacığı havuzunu kapatmak için kullanılır
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Worker sınıfı: Callable<Integer> arayüzünü uygulayan iş parçacığı sınıfı
    private static class Worker implements Callable<Integer> { // Calleble arayüzü, bir işlemin sonucunu döndürmek için kullanılır.
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
