package org.example;
import javax.swing.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class EmailGonder extends JFrame {

    private JTextField txtEmail;
    private JTextField txtKelime;
    private int toplam;
    private JFrame frameEmail;

    public EmailGonder(String kelime, int toplamSayi) {
        this.txtKelime = new JTextField(kelime);
        this.toplam = toplamSayi;

        //ekrana email pop-up ı çıkar
        frameEmail = new JFrame("Email Gönderme");
        frameEmail.setSize(400, 200);
        frameEmail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameEmail.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblEmail = new JLabel("Email adresinizi giriniz:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frameEmail.add(lblEmail, gbc);

        txtEmail = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        txtEmail.setPreferredSize(new Dimension(200, 18));
        frameEmail.add(txtEmail, gbc);

        JButton btnGonder = new JButton("Gönder");
        gbc.gridx = 1;
        gbc.gridy = 1;
        frameEmail.add(btnGonder, gbc);

        btnGonder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                email();
            }
        });

        frameEmail.setLocationRelativeTo(null);
        frameEmail.setVisible(true);
    }

    public void email() {
        String emailGiris = txtEmail.getText().trim();
        String kelime = txtKelime.getText();

        if (emailGiris.isEmpty()) {
            JOptionPane.showMessageDialog(frameEmail, "Lütfen geçerli bir e-posta adresi giriniz.");
            return;
        }

        // Mail gönderme işlemi
        final String userName = "suedanursarican233@gmail.com"; // Gönderen e-posta adresi
        final String password = "qljp rdoh qxry tufx"; // Gönderen e-posta şifresi

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
            MimeMessage mesaj = new MimeMessage(session);
            mesaj.setFrom(new InternetAddress(userName)); // E-posta göndericisini belirler
            mesaj.addRecipient(Message.RecipientType.TO, new InternetAddress(emailGiris));
            mesaj.setSubject("Dosyada kelime bulma");
            mesaj.setText("Merhabalar,\n\nİlgili metin dosyasındaki kelime sayısını hesaplandı. Sonuçlar aşağıdaki gibidir:\n\n- Aranan Kelime: " + kelime + "\n- Toplam Bulunan Sayı: " + toplam);
            System.out.println("Mail Gönderiliyor...");
            Transport.send(mesaj);
            System.out.println("Mail Başarıyla Gönderildi.");
            JOptionPane.showMessageDialog(frameEmail, "Mail başarıyla gönderildi.");
        } catch (AddressException ae) {
            JOptionPane.showMessageDialog(frameEmail, "Geçersiz e-posta adresi: " + emailGiris);
        } catch (MessagingException me) {
            me.printStackTrace();
            JOptionPane.showMessageDialog(frameEmail, "E-posta gönderilirken bir hata oluştu: " + me.getMessage());
        }
    }
}
