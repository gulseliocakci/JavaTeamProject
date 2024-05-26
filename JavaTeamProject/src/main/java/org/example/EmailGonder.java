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
    private int total;
    private JFrame frame;

    public EmailGonder(String kelime, int totalCount) {
        this.txtKelime = new JTextField(kelime);
        this.total = totalCount;

        // Initialize the frame
        frame = new JFrame("Email Gönderme");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblEmail = new JLabel("Email adresinizi giriniz:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(lblEmail, gbc);

        txtEmail = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        txtEmail.setPreferredSize(new Dimension(200, 18));
        frame.add(txtEmail, gbc);

        JButton btnGonder = new JButton("Gönder");
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(btnGonder, gbc);

        btnGonder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                email();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void email() {
        String emailInput = txtEmail.getText().trim();
        String kelime = txtKelime.getText();

        if (emailInput.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Lütfen geçerli bir e-posta adresi giriniz.");
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
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailInput));
            message.setSubject("Dosyada kelime bulma");
            message.setText("Merhabalar,\n" +
                    "\n" +
                    "İlgili metin dosyasındaki kelime sayısını hesaplandı. Sonuçlar aşağıdaki gibidir:\n" +
                    "\n" +
                    "- Aranan Kelime: " + kelime + "\n" +
                    "- Toplam Bulunan Sayı: " + total);
            System.out.println("Mail Gönderiliyor...");
            Transport.send(message);
            System.out.println("Mail Başarıyla Gönderildi....");
            JOptionPane.showMessageDialog(frame, "Mail Başarıyla Gönderildi....");
        } catch (AddressException ae) {
            JOptionPane.showMessageDialog(frame, "Geçersiz e-posta adresi: " + emailInput);
        } catch (MessagingException me) {
            me.printStackTrace();
            JOptionPane.showMessageDialog(frame, "E-posta gönderilirken bir hata oluştu: " + me.getMessage());
        }
    }
}
