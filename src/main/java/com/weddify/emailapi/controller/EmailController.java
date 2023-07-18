package com.weddify.emailapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RestController;
import com.weddify.emailapi.entity.User;
import com.weddify.emailapi.service.EmailService;

@RestController
@PropertySource("classpath:application.properties")
public class EmailController {

    @Autowired
    private EmailService EmailService;

    @Value("${spring.mail.username}")
    private String to;
    
    @RabbitListener(queues = "contactus")
    public void contactusEmail(byte[] byteArr) throws MessagingException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
    	ObjectMapper objectMapper = new ObjectMapper();
		String jsonStr = new String(byteArr, "UTF-8");
		User user = objectMapper.readValue(jsonStr, User.class);
		EmailService.sendEmail(user, "ContactUsEmail", to);
    }

    @RabbitListener(queues = "register")
    public void registerEmail(byte[] byteArr) throws MessagingException, UnsupportedEncodingException, JsonMappingException, JsonProcessingException {    
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            EmailService.sendEmail(user, "WelcomeEmail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "book")
    public void BookEmail(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{ 
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmailWithAttachment(user, "BookingEmail");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @RabbitListener(queues = "bookvendor")
    public void BookEmailVendor(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{    
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmail(user, "BookingEmailVendor");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "cancelbook")
    public void BookCancelEmail(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmail(user, "BookingCancelEmail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "cancelbookvendor")
    public void BookCancelEmailVendor(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{    
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmail(user, "BookingCancelEmailVendor");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "delete")
    public void DeleteEmail(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{    
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmail(user, "GoodbyeEmail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "resetpwd")
    public void ResetpwdEmail(byte[] byteArr) throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException{    
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	String jsonStr = new String(byteArr, "UTF-8");
        	User user = objectMapper.readValue(jsonStr, User.class);
            this.EmailService.sendEmail(user, "ResetpwdEmail");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}