package application.configurations;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class AzatMailSender {


    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toAddress, String subject, String messageBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress("kot514877@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setText(messageBody, "UTF-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendHtmlEmail(String toAddress, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            //message.setFrom(new InternetAddress("kot514877@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }


    public void sendHtmlEmailWithAttachment(String toAddress, String subject, String htmlContent, File attachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(subject);
            message.setRecipients(MimeMessage.RecipientType.TO, toAddress);

            // Создаем HTML-часть
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            // Создаем вложение (изображение)
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(attachment.getName());

            // Объединяем в Multipart
            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
