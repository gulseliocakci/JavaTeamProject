package org.example;

import javax.mail.internet.AddressException;
import javax.swing.*;
import javax.swing.text.*;
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
        frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame1.setLayout(new BorderLayout());


        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        gbc.gridx = 1;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Aranacak Kelime:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        JTextArea txtKelime = new JTextArea(1, 20);
        topPanel.add(txtKelime, gbc);

        // Kelime ve karakter sayısı etiketleri
        gbc.gridx = 1;
        gbc.gridy = 2;
        JLabel lblKelime = new JLabel("Kelime sayısı:");
        topPanel.add(lblKelime, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblKarakter = new JLabel("Karakter sayısı:");
        topPanel.add(lblKarakter, gbc);

        // Dosya seçme butonu
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridy = 0;
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

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame1.add(scrollPane, BorderLayout.CENTER);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JFileChooser fileChooser = new JFileChooser();

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


        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kelime = txtKelime.getText();
                Cekirdek cekirdek = new Cekirdek(fileChooser.getSelectedFile(), kelime);

                cekirdek.cekirdeklereBolme();
                int total = cekirdek.getTotalCount();
                highlightWords(textArea, kelime);

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
                    EmailGonder email = new EmailGonder(kelime, total);
                }
            }
        });

        frame1.setVisible(true);
    }

    private static void highlightWords(JTextArea textArea, String word) {
        try {
            Highlighter highlighter = textArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            String text = textArea.getText();
            int pos = 0;

            while ((pos = text.indexOf(word, pos)) >= 0) {
                highlighter.addHighlight(pos, pos + word.length(), painter);
                pos += word.length();
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
