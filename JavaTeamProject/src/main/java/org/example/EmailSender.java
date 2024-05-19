package org.example;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Scanner;

public class EmailSender {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Email adresiniz: ");
//        String emailInput = scanner.nextLine();

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
}
