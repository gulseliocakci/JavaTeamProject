package org.example.demo4;

import socketapp.Cekirdek;

import javax.mail.internet.AddressException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.net.URL;

public class GUI {

    public static void GUIPlay() {
        // Swing GUI

        JFrame frame1 = new JFrame("Paralel Dosya");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tam ekran modu
        frame1.setLayout(new BorderLayout());

        // Üst panel: arama kelimesi ve dosya seçimi
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Arama kelimesi alanı
        gbc.gridx = 1;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Aranacak Kelime:"), gbc);

        gbc.gridx = 2;
        JTextArea txtKelime = new JTextArea(1, 20);
        topPanel.add(txtKelime, gbc);

        // Kelime ve karakter sayısı etiketleri
        gbc.gridx = 3;
        JLabel lblKelime = new JLabel("Kelime sayısı:");
        topPanel.add(lblKelime, gbc);

        gbc.gridx = 5;
        JLabel lblKarakter = new JLabel("Karakter sayısı:");
        topPanel.add(lblKarakter, gbc);

        // Dosya seçme butonu
        gbc.gridx = 0;
        JButton btnChooseFile = new JButton();
        try {
            URL selectButtonUrl = new URL("https://w7.pngwing.com/pngs/1015/894/png-transparent-computer-icons-directory-%E5%9B%BE%E6%A0%87-purple-image-file-formats-violet.png");
            ImageIcon selectButtonIcon = new ImageIcon(selectButtonUrl);
            Image selectButtonImage = selectButtonIcon.getImage();
            Image scaledSelectButtonImage = selectButtonImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);
            btnChooseFile.setIcon(new ImageIcon(scaledSelectButtonImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnChooseFile.setPreferredSize(new Dimension(70, 60));
        topPanel.add(btnChooseFile, gbc);

        // Arama butonu
        gbc.gridx = 7;
        JButton btnSearch = new JButton();
        try {
            URL searchButtonUrl = new URL("https://w1.pngwing.com/pngs/950/465/png-transparent-search-icon-search-icon-search-line-icon-icon-pink-magenta-circle.png");
            ImageIcon searchButtonIcon = new ImageIcon(searchButtonUrl);
            Image searchButtonImage = searchButtonIcon.getImage();
            Image scaledSearchButtonImage = searchButtonImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);
            btnSearch.setIcon(new ImageIcon(scaledSearchButtonImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnSearch.setPreferredSize(new Dimension(70, 60));
        topPanel.add(btnSearch, gbc);

        frame1.add(topPanel, BorderLayout.NORTH);

        // Metin alanı ve kaydırma paneli
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame1.add(scrollPane, BorderLayout.CENTER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JFileChooser fileChooser = new JFileChooser();

        // Dosya seçme butonu işlemi
        btnChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        textArea.read(new FileReader(file.getAbsolutePath()), null);
                        textArea.setCaretPosition(0);
                    } catch (IOException ex) {
                        System.out.println("Dosyaya ulaşırken bir hata oluştu: " + file.getAbsolutePath());
                    }
                } else {
                    System.out.println("Seçim kullanıcı tarafından iptal edildi.");
                }
                String text = textArea.getText();
                String[] words = text.split("\\s+");
                lblKelime.setText("Kelime sayısı: " + words.length);
                lblKarakter.setText("Karakter sayısı: " + (text.length() - 1));
            }
        });

        // Arama butonu işlemi
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kelime = txtKelime.getText();
                Cekirdek cekirdek = new Cekirdek(fileChooser.getSelectedFile(), kelime);

                cekirdek.cekirdeklereBolme();
                int total = cekirdek.getTotalCount();
                String[] options = {"Evet, lütfen.", "Hayır, teşekkürler."};
                int result = JOptionPane.showOptionDialog(
                        frame1,
                        txtKelime.getText() + " kelimesi metinde " + total + " kere geçmektedir. \nSonucun E-Mail adresinize gönderilmesini ister misiniz?",
                        "Swing Tester",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,     // no custom icon
                        options,  // button titles
                        options[0] // default button
                );

                if (result == JOptionPane.YES_OPTION) {
                    JFrame frame2 = new JFrame("Mail");
                    frame2.setSize(400, 200);
                    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame2.setLayout(new GridBagLayout());
                    GridBagConstraints gbc2 = new GridBagConstraints();
                    gbc2.insets = new Insets(5, 5, 5, 5);

                    JLabel lblEmail = new JLabel("Email adresinizi giriniz:");
                    gbc2.gridx = 0;
                    gbc2.gridy = 0;
                    frame2.add(lblEmail, gbc2);

                    JTextArea txtEmail = new JTextArea();
                    gbc2.gridx = 1;
                    gbc2.gridy = 0;
                    txtEmail.setPreferredSize(new Dimension(200, 18));
                    frame2.add(txtEmail, gbc2);

                    JButton btnGiris = new JButton("Gönder");
                    gbc2.gridx = 1;
                    gbc2.gridy = 1;
                    frame2.add(btnGiris, gbc2);

                    btnGiris.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String emailInput = txtEmail.getText().trim();

                            if (emailInput.isEmpty()) {
                                JOptionPane.showMessageDialog(frame1, "Lütfen geçerli bir e-posta adresi giriniz.");
                                return;
                            }

                            // Mail gönderme işlemi
                            final String userName = "suedanursarican233@gmail.com"; // Gönderen e-posta adresi
                            final String password = "t q s f h q s d s x l w j c v q"; // Gönderen e-posta şifresi

                            Properties props = new Properties();
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");

                            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(userName, password);
                                }
                            });

                            try {
                                MimeMessage message = new MimeMessage(session);
                                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInput));
                                message.setSubject("Dosyada kelime bulma");
                                message.setText("Merhabalar,\n" +
                                        "\n" +
                                        "İlgili metin dosyasındaki kelime sayısını hesaplandı. Sonuçlar aşağıdaki gibidir:\n" +
                                        "\n" +
                                        "-Aranan Kelime: " + txtKelime.getText() + "\n" +
                                        "-Toplam Bulunan Sayı: " + total);
                                System.out.println("Mail Gönderiliyor...");
                                Transport.send(message);
                                System.out.println("Mail Başarıyla Gönderildi....");
                                JOptionPane.showMessageDialog(frame2, "Mail Başarıyla Gönderildi....");
                            } catch (AddressException ae) {
                                JOptionPane.showMessageDialog(frame2, "Geçersiz e-posta adresi: " + emailInput);
                            } catch (MessagingException me) {
                                me.printStackTrace();
                            }
                        }
                    });
                    frame2.setLocationRelativeTo(null);
                    frame2.setVisible(true);
                }
            }
        });

        frame1.setVisible(true);
    }
}
