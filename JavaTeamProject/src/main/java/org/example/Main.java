package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        JFrame f= new JFrame("paralel dosya");
        f.setSize(750,750);
        JFrame f2=new JFrame("mail");
        f2.setSize(400,200);
        JLabel l3=new JLabel("emailinizi giriniz");
        l3.setBounds(20,0,150,100);
        JTextArea s2=new JTextArea();
        s2.setBounds(150,39,200,18);
        JButton btn3=new JButton("giris");
        btn3.setBounds(150,100,100,25);

        JTextArea s=new JTextArea();
        s.setBounds(230,25,250,20);
        JLabel l1=new JLabel("kelime sayısı:");
        l1.setBounds(140,28,200,100);
        JLabel l2=new JLabel("karakter sayısı:");
        l2.setBounds(400,28,200,100);

        JTextArea textarea=new JTextArea();
        textarea.setBounds(0,100,750,680);
        JButton btn2=new JButton("search");
        btn2.setBackground(Color.PINK);
        btn2.setForeground(Color.PINK);
        btn2.setBounds(505,10,100,50);
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text =textarea.getText();
                String words[] =text.split("\\s+");
                l1.setText("kelime sayısı:"+(words.length));
                l2.setText("karakter sayısı:"+(text.length()-1));

            }
        });

        JLabel lb=new JLabel("search word");
        lb.setBounds(140,0,100,70);
        JFileChooser j=new JFileChooser();
        JButton btn=new JButton("seç");
        btn.setBackground(Color.MAGENTA);
        btn.setForeground(Color.MAGENTA);
        btn.setBounds(0,10,100,50);
        btn.addActionListener(new ActionListener() {
                                  @Override
                                  public void actionPerformed(ActionEvent e) {
                                      int returnVal = j.showOpenDialog(null);
                                      if (returnVal == JFileChooser.APPROVE_OPTION) {
                                          File file = j.getSelectedFile();
                                          try {
                                              //Burada ne yapmak istiyorsak yaparız biz text area da göstereceğiz.
                                              textarea.read( new FileReader( file.getAbsolutePath() ), null );
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

    }}

