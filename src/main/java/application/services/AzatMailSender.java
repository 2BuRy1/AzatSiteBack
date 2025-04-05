package application.services;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@EnableAsync
public class AzatMailSender {



    private final JavaMailSender mailSender;


    private final FileService fileService;


    @Autowired
    public AzatMailSender(JavaMailSender mailSender, FileService fileService){
        this.mailSender = mailSender;
        this.fileService = fileService;
    }

    public void sendSimpleEmail(String toAddress, String subject, String messageBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
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
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }

    @Async
    public void sendHtmlEmailWithAttachment(String toAddress, String subject, String htmlContent, File attachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject(subject);
            message.setRecipients(MimeMessage.RecipientType.TO, toAddress);


            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(attachment.getName());

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            mailSender.send(message);
            fileService.removeFileByName(attachment.getName());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email with attachment", e);
        }
    }
}
