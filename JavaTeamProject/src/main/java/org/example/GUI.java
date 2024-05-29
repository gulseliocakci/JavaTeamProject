package org.example;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class GUI{
    private static File dosyaSecme = null;
    private static String sonArananKelime = "";
    private static int sunucuSayisi;

    public static int getSunucuSayisi() {
        return sunucuSayisi;
    }

    public static void GUIPlay() {
         // Swing GUI

        JFrame frame1 = new JFrame("Paralel Dosya"); //açılan ilk ekran
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tam ekran modu
        frame1.setLayout(new BorderLayout());

        // Üst panel: arama kelimesi ve dosya seçimi
        JPanel ustPanel = new JPanel();
        ustPanel.setBackground(new Color(246, 239, 239));
        ustPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); //
        gbc.insets = new Insets(7, 7, 7, 7);

        // Arama kelimesi alanı
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel lblaranacakKelime = new JLabel("Aranacak Kelime:");
        lblaranacakKelime.setFont(new Font(lblaranacakKelime.getFont().getName(), Font.PLAIN, lblaranacakKelime.getFont().getSize() + 5));
        ustPanel.add(lblaranacakKelime ,gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        JTextArea txtKelime = new JTextArea(1, 20);
        ustPanel.add(txtKelime, gbc);
        String placeholderText = "Buraya yazınız...";

        txtKelime.setText(placeholderText);
        txtKelime.setForeground(Color.GRAY);
        Font placeholderFont = txtKelime.getFont().deriveFont(Font.ITALIC, txtKelime.getFont().getSize() + 5);
        txtKelime.setFont(placeholderFont);

        txtKelime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtKelime.getText().equals(placeholderText)) {
                    txtKelime.setText("");
                    txtKelime.setForeground(Color.BLACK);
                    txtKelime.setFont(txtKelime.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtKelime.getText().isEmpty()) {
                    txtKelime.setText(placeholderText);
                    txtKelime.setForeground(Color.GRAY);
                    txtKelime.setFont(txtKelime.getFont().deriveFont(Font.ITALIC));
                }
            }
        });

        // Kelime ve karakter sayısı etiketleri
        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel lblKelime = new JLabel("Kelime sayısı:");
        lblKelime.setFont(new Font(lblKelime.getFont().getName(), Font.PLAIN, lblKelime.getFont().getSize() + 5));
        ustPanel.add(lblKelime, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblKarakter = new JLabel("Karakter sayısı:");
        lblKarakter.setFont(new Font(lblKarakter.getFont().getName(), Font.PLAIN, lblKarakter.getFont().getSize() + 5));
        ustPanel.add(lblKarakter, gbc);

        // Dosya seçme butonu
        gbc.gridx = 0;
        gbc.gridy = 0;
        JButton btnDosya = new JButton();
        try {
            ImageIcon selectButtonIcon = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/resources/dosya.png")));
            Image selectButtonImage = selectButtonIcon.getImage();
            Image scaledSelectButtonImage = selectButtonImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btnDosya.setIcon(new ImageIcon(scaledSelectButtonImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnDosya.setPreferredSize(new Dimension(80, 80));
        ustPanel.add(btnDosya, gbc);

        // Arama butonu
        gbc.gridx = 8;
        gbc.gridy = 0;
        JButton btnArama = new JButton();
        try {
            ImageIcon btnAramaIkonu = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/resources/aramaicon.png"))); // Resmi dosya yolundan yükle
            Image aramaButonuResmi = btnAramaIkonu.getImage();
            Image aramaButonuResmiBoyutu = aramaButonuResmi.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
            btnArama.setIcon(new ImageIcon(aramaButonuResmiBoyutu));
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnArama.setPreferredSize(new Dimension(70, 70));
        ustPanel.add(btnArama, gbc);

        txtKelime.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Enter tuşuna basıldığında btnSearch çalışsın
                    btnArama.doClick();
                }
            }
        });

        frame1.add(ustPanel, BorderLayout.NORTH);

        gbc.gridx = 7;
        gbc.gridy = 0;
        String[] secenekler = {"-Dosyayı gönderme yöntemi seçiniz.-","Bilgisayarlara gönder.", "Çekirdeklere gönder."};
        JComboBox<String> comboBox = new JComboBox<>(secenekler);
        comboBox.setBackground(new Color(246, 239, 239));
        Font comboBoxFont = comboBox.getFont().deriveFont(comboBox.getFont().getSize() + 3f);
        comboBox.setFont(comboBoxFont);
        ustPanel.add(comboBox, gbc);

        // JRadioButton'lar için panel-bilgisayar sayısı seçmek için
        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(new Color(246, 239, 239));
        radioPanel.setVisible(false);
        // Başlangıçta görünmüyor
        gbc.gridx = 9;
        gbc.gridy = 0;
        JLabel lblbilgisayar = new JLabel("Bilgisayar sayısı:");
        lblbilgisayar.setVisible(false);

        Font labelFont = lblbilgisayar.getFont().deriveFont(lblbilgisayar.getFont().getSize() + 5f);
        lblbilgisayar.setFont(labelFont);

        ustPanel.add(lblbilgisayar, gbc);

        JRadioButton radioButton1 = new JRadioButton("1");
        JRadioButton radioButton2 = new JRadioButton("2");
        JRadioButton radioButton3 = new JRadioButton("3");

        Font radioButtonFont = radioButton1.getFont().deriveFont(radioButton1.getFont().getSize() + 5f);
        radioButton1.setFont(radioButtonFont);
        radioButton2.setFont(radioButtonFont);
        radioButton3.setFont(radioButtonFont);

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(radioButton1);
        radioGroup.add(radioButton2);
        radioGroup.add(radioButton3);

        radioPanel.add(radioButton1);
        radioPanel.add(radioButton2);
        radioPanel.add(radioButton3);

        gbc.gridx = 10;
        gbc.gridy = 0;
        ustPanel.add(radioPanel, gbc);

        radioButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sunucuSayisi =1;


            }
        });
        radioButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sunucuSayisi =2;

            }
        });
        radioButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sunucuSayisi =3;

            }
        });


        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedItem().equals("Bilgisayarlara gönder.")) {
                    radioPanel.setVisible(true);
                    lblbilgisayar.setVisible(true);

                } else {
                    radioPanel.setVisible(false);
                    lblbilgisayar.setVisible(false);
                }
                frame1.revalidate();
                frame1.repaint();
            }
        });


        // Metin alanı ve kaydırma paneli
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame1.add(scrollPane, BorderLayout.CENTER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JFileChooser fileChooser = new JFileChooser();

        btnDosya.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                // Yeni bir JDialog oluştur
                JDialog dialog = new JDialog(frame1, "Dosya İşlemleri", true);
                dialog.setLayout(new GridBagLayout());
                dialog.setSize(300, 150);
                dialog.setLocationRelativeTo(frame1); // Dialogu ortalar

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(5, 5, 5, 5);

                // Mesaj etiketi
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 2;
                dialog.add(new JLabel("Lütfen bir işlem seçiniz:"), gbc);

                // Dosya Seç butonu
                JButton btnDosyaSec = new JButton("Dosya Seç");
                btnDosyaSec.setBackground(new Color(246, 239, 239));
                btnDosyaSec.setForeground(Color.BLACK);
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.gridwidth = 1;
                dialog.add(btnDosyaSec, gbc);

                // Yeni Dosya Oluştur butonu
                JButton btnDosyaOlustur = new JButton("Yeni Dosya Oluştur");
                btnDosyaOlustur.setBackground(new Color(246, 239, 239));
                btnDosyaOlustur.setForeground(Color.BLACK);
                gbc.gridx = 1;
                gbc.gridy = 1;
                dialog.add(btnDosyaOlustur, gbc);

                // Dosya Seç butonuna tıklama işlemi
                btnDosyaSec.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // Diyaloğu kapat
                        Font font = textArea.getFont(); // Mevcut font bilgisini al
                        float fontSize = font.getSize() + 5; // Mevcut font boyutunu 5 birim artır
                        textArea.setFont(font.deriveFont(fontSize));
                        textArea.setEditable(false);
                        int returnVal = fileChooser.showOpenDialog(null);
                        if (returnVal == JFileChooser.APPROVE_OPTION) { // Kullanıcı dosya seçtiyse devam et
                            File file = fileChooser.getSelectedFile();
                            try {
                                textArea.read(new FileReader(file.getAbsolutePath()), null); // Dosyayı oku
                                textArea.setCaretPosition(0); // Metin alanını en başa getir
                            } catch (IOException ex) {
                                System.out.println("Dosyaya ulaşırken bir hata oluştu: " + file.getAbsolutePath());
                            }
                            dosyaSecme = file; // Seçilen dosyayı sakla
                            kelimeKarakterSayisiGuncelle(textArea.getText(), lblKelime, lblKarakter);

                        }
                    }
                });


                // Yeni Dosya Oluştur butonuna tıklama işlemi
                btnDosyaOlustur.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // Diyaloğu kapat
                        Font font = textArea.getFont(); // Mevcut font bilgisini al
                        float fontSize = font.getSize() + 5; // Mevcut font boyutunu 5 birim artır
                        textArea.setFont(font.deriveFont(fontSize));

                        String placeholderText = "Buraya yazınız...";
                        textArea.setText(placeholderText);
                        textArea.setForeground(Color.GRAY);
                        textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));

                        textArea.addFocusListener(new FocusListener() {
                            @Override
                            public void focusGained(FocusEvent e) {
                                if (textArea.getText().equals(placeholderText)) {
                                    textArea.setText("");
                                    textArea.setForeground(Color.BLACK);
                                    textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
                                }
                            }

                            @Override
                            public void focusLost(FocusEvent e) {
                                if (textArea.getText().isEmpty()) {
                                    textArea.setText(placeholderText);
                                    textArea.setForeground(Color.GRAY);
                                    textArea.setFont(textArea.getFont().deriveFont(Font.ITALIC));
                                }
                            }
                        });

                        // Kaydet butonunu oluştur ve topPanel'e ekle
                        gbc.gridx = 11;
                        gbc.gridy = 0;
                        JButton btnKaydet = new JButton();
                        try {
                            ImageIcon kaydetButonuIkonu = new ImageIcon(Objects.requireNonNull(GUI.class.getResource("/resources/kaydet.png")));
                            Image kaydetButonuResmi = kaydetButonuIkonu.getImage();
                            Image kaydetButonuResimBoyutu = kaydetButonuResmi.getScaledInstance(113, 30, Image.SCALE_SMOOTH);
                            btnKaydet.setIcon(new ImageIcon(kaydetButonuResimBoyutu));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        btnKaydet.setPreferredSize(new Dimension(113, 30));
                        ustPanel.add(btnKaydet, gbc);

                        // topPanel'i yeniden çiz
                        ustPanel.revalidate();
                        ustPanel.repaint();

                        // Kaydet butonu işlemi
                        btnKaydet.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                fileChooser.setDialogTitle("Kaydet");
                                String text = textArea.getText();
                                String[] kelimeler = text.split("\\s+");
                                lblKelime.setText("Kelime sayısı: " + kelimeler.length);
                                lblKarakter.setText("Karakter sayısı: " + (text.length() - 1));

                                int kullaniciSecimi = fileChooser.showSaveDialog(frame1);
                                if (kullaniciSecimi == JFileChooser.APPROVE_OPTION) {
                                    File kaydedilecekDosya = fileChooser.getSelectedFile();
                                    dosyaSecme = kaydedilecekDosya;
                                    try (FileWriter fileWriter = new FileWriter(kaydedilecekDosya)) {
                                        fileWriter.write(textArea.getText());
                                        JOptionPane.showMessageDialog(frame1, "Dosya başarıyla kaydedildi: " + kaydedilecekDosya.getAbsolutePath() + "\n Şimdi kelime arama yapabilirsiniz.");
                                    } catch (IOException ex) {
                                        JOptionPane.showMessageDialog(frame1, "Dosya kaydedilirken bir hata oluştu.", "Hata", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });
                    }
                });

                dialog.setVisible(true);
            }
        });

        // Arama butonu işlemi
        btnArama.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dosyaSecme == null) {
                    JOptionPane.showMessageDialog(frame1, "Lütfen önce bir dosya kaydedin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String kelime = txtKelime.getText();
                if (kelime.isEmpty()) {
                    JOptionPane.showMessageDialog(frame1, "Lütfen aramak istediğiniz kelimeyi girin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                sonArananKelime = kelime;
                highlightKelime(textArea, sonArananKelime);
                highlightKelime(textArea, kelime);
                int toplam = 0;
                if (Objects.equals(comboBox.getSelectedItem(), "Çekirdeklere gönder.")) {

                    Cekirdek cekirdek = new Cekirdek(dosyaSecme, kelime);
                    cekirdek.cekirdeklereBolme();
                    toplam = cekirdek.getToplamKelimeSayisi();

                } else if (Objects.equals(comboBox.getSelectedItem(),"Bilgisayarlara gönder.")) {
                    Server server=new Server(dosyaSecme, kelime);
                    Server.dosyayiGonder();
                    toplam = server.getToplamKelimeSayisi();

                }


                UIManager.put("OptionPane.yesButtonText", "Evet");
                UIManager.put("OptionPane.noButtonText", "Hayır");

                Color butonRenk = new Color(246, 239, 239);
                UIManager.put("Button.background", butonRenk);
                UIManager.put("Button.foreground", Color.BLACK); // Yazı rengini ayarlanabilir

                String[] secenekler = {"Evet", "Hayır"};
                int sonuc = JOptionPane.showOptionDialog(
                        frame1,
                        "'" + kelime + "'" + " kelimesi metinde " + toplam + " kere geçmektedir. \nSonucun E-Mail adresinize gönderilmesini ister misiniz?",
                        "",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        secenekler,
                        secenekler[0]
                );

                if (sonuc == JOptionPane.YES_OPTION) {
                    org.example.EmailGonder email = new org.example.EmailGonder(kelime, toplam);
                }


            }
        });

        frame1.setVisible(true);
    }
    private static void kelimeKarakterSayisiGuncelle(String text, JLabel lblKelime, JLabel lblKarakter) {
        String[] kelimeler = text.split("\\s+");
        lblKelime.setText("Kelime sayısı: " + kelimeler.length);
        lblKarakter.setText("Karakter sayısı: " + (text.length() - 1));
    }
    private static boolean isTextFile(File file) {
        String[] metinDosyasiUzantilari = {"txt", "java", "xml", "html", "htm", "csv", "json"};
        String dosyaAdi = file.getName();
        for (String extension : metinDosyasiUzantilari) {
            if (dosyaAdi.endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    private static void highlightKelime(JTextArea textArea, String word) {
        try {
            Highlighter highlighter = textArea.getHighlighter();
            highlighter.removeAllHighlights(); // Mevcut tüm highlight'ları kaldır

            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
            String text = textArea.getText().toLowerCase(); // Tüm metni küçük harfe dönüştür
            String arananKelime = word.toLowerCase(); // Aranan kelimeyi küçük harfe dönüştür
            int pos = 0;

            while ((pos = text.indexOf(arananKelime, pos)) >= 0) {
                highlighter.addHighlight(pos, pos + arananKelime.length(), painter);
                pos += arananKelime.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
