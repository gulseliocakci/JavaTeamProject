package org.example;

import com.sun.activation.viewers.TextEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GUI extends EmailSender{
    public void GUI(){

    JFrame frame1= new JFrame("paralel dosya");
        frame1.setSize(750,750);
    JFrame frame2=new JFrame("mail");
        frame2.setSize(400,200);
    JLabel lblEmail=new JLabel("emailinizi giriniz");
        lblEmail.setBounds(20,0,150,100);
    JTextArea txtEmail=new JTextArea();
        txtEmail.setBounds(150,39,200,18);
    JButton btnGiris=new JButton("giris");
        btnGiris.setBounds(150,100,100,25);

    JTextArea txtKelime=new JTextArea();
        txtKelime.setBounds(230,25,250,20);
    JLabel lblKelime=new JLabel("kelime sayısı:");
        lblKelime.setBounds(140,28,200,100);
    JLabel lblKarakter=new JLabel("karakter sayısı:");
        lblKarakter.setBounds(400,28,200,100);

    JTextArea textArea=new JTextArea();
        textArea.setBounds(0,100,750,680);
    JButton btnSearch=new JButton("search");
        btnSearch.setBackground(Color.PINK);
        btnSearch.setForeground(Color.PINK);
        btnSearch.setBounds(505,10,100,50);
        btnSearch.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text =textArea.getText();
            String words[] =text.split("\\s+");
            lblKelime.setText("kelime sayısı:"+(words.length));
            lblKarakter.setText("karakter sayısı:"+(text.length()-1));

        }
    });


    JLabel lb=new JLabel("search word");
        lb.setBounds(140,0,100,70);
    JFileChooser j=new JFileChooser();
    JButton btnSec=new JButton("seç");
        btnSec.setBackground(Color.MAGENTA);
        btnSec.setForeground(Color.MAGENTA);
        btnSec.setBounds(0,10,100,50);
        btnSec.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = j.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = j.getSelectedFile();
                try {
                    //Burada ne yapmak istiyorsak yaparız biz text area da göstereceğiz.
                    textArea.read( new FileReader( file.getAbsolutePath() ), null );
                } catch (IOException ex) {
                    System.out.println("Dosyaya ulaşırken bir hata oluştu."
                            +file.getAbsolutePath());
                }
            } else {
                System.out.println("Seçim kullanıcı tarafından iptal edildi. ");
            }
        }
    }
        );

        btnGiris.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            frame2.setVisible(false);
            frame1.add(btnSec);
            frame1.add(lb);
            frame1.add(txtKelime);
            frame1.add(btnSearch);
            frame1.add(lblKelime);
            frame1.add(lblKarakter);
            frame1.add(textArea);
            frame1.setLayout(null);
            frame1.setVisible(true);

        }
    });

        frame2.add(lblEmail);
        frame2.add(txtEmail);
        frame2.add(btnGiris);
        frame2.setLayout(null);
        frame2.setVisible(true);
        }

}

