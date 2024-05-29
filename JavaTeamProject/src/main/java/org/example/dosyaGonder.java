package org.example;

import java.io.File;
import java.io.Serializable;

public abstract class dosyaGonder implements Serializable {
    protected File dosyaYolu;
    protected String arananKelime;
    protected int toplamKelimeSayisi;

    public dosyaGonder(File dosyaYolu, String arananKelime) {
        this.dosyaYolu = dosyaYolu;
        this.arananKelime = arananKelime;
    }

    public int getToplamKelimeSayisi() {
        return toplamKelimeSayisi;
    }

    public abstract void dosyaBol();

    public void dosyayiIsle() {
        // Alt sınıflar tarafından geçersiz kılınacak
    }
}
