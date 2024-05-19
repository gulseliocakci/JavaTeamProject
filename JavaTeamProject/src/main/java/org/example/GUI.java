package org.example;

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

public class GUI {
    public static void GUIPlay(){
        // Swing GUI
        JFrame f = new JFrame("paralel dosya");
        f.setSize(750, 750);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFrame f2 = new JFrame("mail");
        f2.setSize(400, 200);
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel l3 = new JLabel("Email adresinizi giriniz:");
        l3.setBounds(20, 0, 200, 100);

        JTextArea s2 = new JTextArea();
        s2.setBounds(200, 39, 200, 18);

        JButton btn3 = new JButton("Giriş");
        btn3.setBounds(200, 100, 100, 25);

        JTextArea s = new JTextArea();
        s.setBounds(230, 25, 250, 20);

        JLabel l1 = new JLabel("kelime sayısı:");
        l1.setBounds(140, 28, 200, 100);

        JLabel l2 = new JLabel("karakter sayısı:");
        l2.setBounds(400, 28, 200, 100);

        JTextArea textarea = new JTextArea();
        textarea.setBounds(0, 100, 750, 680);

        JButton btn2 = new JButton();
        btn2.setBackground(Color.white);
        btn2.setForeground(Color.PINK);
        btn2.setBounds(535, 10, 50, 60); // Set button size to 100x50

        // Load the image for the "search" button
        String searchButtonImagePath = "C:\\Users\\sueda\\OneDrive\\Masaüstü\\grisearch.png";
        ImageIcon searchButtonIcon = new ImageIcon(searchButtonImagePath);
        Image searchButtonImage = searchButtonIcon.getImage();
        Image scaledSearchButtonImage = searchButtonImage.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
        btn2.setIcon(new ImageIcon(scaledSearchButtonImage));

        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textarea.getText();
                String[] words = text.split("\\s+");
                l1.setText("kelime sayısı:" + (words.length));
                l2.setText("karakter sayısı:" + (text.length() - 1));

                String emailInput = s2.getText(); // Kullanıcının girdiği e-posta adresi

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
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInput, true));
                    message.setSubject("Mail Gönderildi");
                    message.setText("Mail gönderildi.");
                    System.out.println("Mail Gönderiliyor...");
                    Transport.send(message);
                    System.out.println("Mail Başarıyla Gönderildi....");
                } catch (MessagingException me) {
                    System.out.println("Exception: " + me);
                }
            }
        });

        JLabel lb = new JLabel("search word");
        lb.setBounds(140, 0, 100, 70);

        JFileChooser j = new JFileChooser();

        // Load the image for the "seç" button
        String selectButtonImagePath = "C:\\Users\\sueda\\OneDrive\\Masaüstü\\pembedosya.png";
        JButton btn = new JButton();
        btn.setBounds(20, 15, 70, 60); // Set button size to 100x100
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = j.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = j.getSelectedFile();
                    try {
                        textarea.read(new FileReader(file.getAbsolutePath()), null);
                    } catch (IOException ex) {
                        System.out.println("Dosyaya ulaşırken bir hata oluştu: " + file.getAbsolutePath());
                    }
                } else {
                    System.out.println("Seçim kullanıcı tarafından iptal edildi.");
                }
            }
        });

        // Add the image to the button and scale it to fit the button size
        ImageIcon selectButtonIcon = new ImageIcon(selectButtonImagePath);
        Image selectButtonImage = selectButtonIcon.getImage();
        Image scaledSelectButtonImage = selectButtonImage.getScaledInstance(70, 60, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(scaledSelectButtonImage));

        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                f2.setVisible(false);
                f.add(btn);
                f.add(lb);
                f.add(s);
                f.add(btn2);
                f.add(l1);
                f.add(l2);
                f.add(textarea);
                f.setLayout(null);
                f.setVisible(true);
            }
        });

        f2.add(l3);
        f2.add(s2);
        f2.add(btn3);
        f2.setLayout(null);
        f2.setVisible(true);
    }
}

