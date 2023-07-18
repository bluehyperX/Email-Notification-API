package com.weddify.emailapi.service;

import java.io.IOException;
import java.net.URL;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.weddify.emailapi.entity.User;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailsender;
    @Autowired
    private TemplateEngine templateEngine;

    
    public void sendEmail(User user, String templateName, String to) {
        try {
        	Context context = new Context();
            context.setVariable("user", user);

            String process = templateEngine.process(templateName, context);
            MimeMessage message = mailsender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setSubject("New Message from " + user.getFirstname());
            helper.setText(process, true);
            helper.setTo(to);
            helper.setFrom("YOUR-EMAIL-ADDRESS-HERE");
            mailsender.send(message);
        } catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }

    public void sendEmailWithAttachment(User user, String templateName) throws MessagingException, IOException {
        try {
        	Context context = new Context();
            context.setVariable("user", user);

            String process = templateEngine.process(templateName, context);
            MimeMessage message = mailsender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setSubject("Hello " + user.getFirstname());
            helper.setText(process, true);
            helper.setTo(user.getEmail());
            helper.setFrom("YOUR-EMAIL-ADDRESS-HERE");
                
         // Extract the filename from the S3 URL and limit it to the substring before ".pdf" extension
            String filename = user.getUsername().substring(user.getUsername().lastIndexOf('/') + 1);
            int dotIndex = filename.lastIndexOf(".pdf");
            if (dotIndex > 0) {
                filename = filename.substring(0, dotIndex + 4);  // Include the ".pdf" extension
            }

            // Attach the PDF file from the S3 URL with the extracted filename
            URL url = new URL(user.getUsername());
            DataSource source = new URLDataSource(url);
            helper.addAttachment(filename, source);

            mailsender.send(message);
        } 
        catch (MessagingException exception) {
            exception.printStackTrace();
        }
    }
    public void sendEmail(User user, String templateName) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", user);

        String process = templateEngine.process(templateName, context);
        MimeMessage message = mailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Hello " + user.getFirstname());
        helper.setText(process, true);
        helper.setTo(user.getEmail());
        helper.setFrom("YOUR-EMAIL-ADDRESS-HERE");
        mailsender.send(message);
    }
}
