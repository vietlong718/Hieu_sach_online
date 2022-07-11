package com.ute.bookstoreonlinebe.services.mailsender;

import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.entities.embedded.EmbeddedBookInOrder;
import com.ute.bookstoreonlinebe.models.EmailDetails;
import com.ute.bookstoreonlinebe.models.MyConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailSenderServiceImpl implements MailSenderService {

    private JavaMailSender javaMailSender;

    private final Configuration configuration;

    private MyConstants myConstants = new MyConstants();

    public MailSenderServiceImpl(JavaMailSender javaMailSender, Configuration configuration) {
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    @Override
    public void sendMailSignup(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("Chao mung ban den voi FESHBOOK");
            helper.setTo(user.getEmail());
            String emailContent = getEmailSignupContent(user);
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException me){}
        catch (TemplateException te){}
        catch (IOException ie){}
    }

    @Override
    public void sendMailNewOrder(User user, Order order) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("FESHBOOK thông báo đơn đặt hàng");
            helper.setTo(user.getEmail());
            String emailContent = getEmailOrderContent(user, order);
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException me){}
        catch (TemplateException te){}
        catch (IOException ie){}
    }

    @Override
    public void sendMailCallOffOrder(User user, Order order) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("FESHBOOK thông báo hủy đơn hàng");
            helper.setTo(user.getEmail());
            String emailContent = getEmailCallOfOrderContent(user, order);
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException me){}
        catch (TemplateException te){}
        catch (IOException ie){}
    }

    @Override
    public void sendMailChangePassword(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject("FESHBOOK thông báo thay đổi mật khẩu");
            helper.setTo(user.getEmail());
            String emailContent = getEmailChangePasswordContent(user);
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException me){}
        catch (TemplateException te){}
        catch (IOException ie){}
    }

    @Override
    public void sendMailForgotPassword(User user) {

    }

    public void sendEmail(User user) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome To SpringHow.com");
        helper.setTo(user.getEmail());
        String emailContent = getEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }
    String getEmailContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("email.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    String getEmailSignupContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("email-signup.ftl").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    String getEmailOrderContent(User user, Order order) throws IOException, TemplateException {
        List<EmbeddedBookInOrder> books = order.getBooksInOrder();

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("order", order);
        model.put("orderDate", order.getOrderDate().toString());
        model.put("books", books);
        model.put("sumTotal", order.getSubtotal().getPrice());
        configuration.getTemplate("email-order.ftl").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    String getEmailCallOfOrderContent(User user, Order order) throws IOException, TemplateException {

        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("order", order);
        model.put("time", LocalDateTime.now().with(LocalDateTime.MAX).toString());
        configuration.getTemplate("email-call-off-order.ftl").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    String getEmailChangePasswordContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("email-change-password.ftl").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
    String getEmailForgotPasswordContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("email-forgot-password.ftl").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }


    // To send a simple email
//    public void sendSimpleMail(EmailDetails details) throws MessagingException, IOException, TemplateException {
//
//        // Try block to check for exceptions
//        try {
//
//            // Creating a simple mail message
//            SimpleMailMessage mailMessage
//                    = new SimpleMailMessage();
//
//            // Setting up necessary details
//            mailMessage.setFrom(myConstants.MY_EMAIL);
//            mailMessage.setTo(details.getRecipient());
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//
//            // Sending the mail
//            javaMailSender.send(mailMessage);
//            return "Mail Sent Successfully...";
//        }
//
//        // Catch block to handle the exceptions
//        catch (Exception e) {
//            return "Error while Sending Mail";
//        }
//    }
//
//    @Override
//    public void sendMailSignup(User user){
//        String subject = "Welcome to FESHBOOK!";
//        String msgBody = String.format("Chào mừng %s đã đến với FESHBOOK!", user.getFullname());
//
//        return sendSimpleMail(new EmailDetails(user.getEmail(), msgBody, subject, null));
//    }
//
//    @Override
//    public void sendMailNewOrder(User user, Order order) {
//        String subject = "Welcome FESHBOOk!";
//        String msgBodyXC = String.format("Xin chào:  %s", user.getFullname());
//        String msgBodyCT = String.format("\nBạn đã đặt hàng thành công, với mã đơn hàng là: %s ", order.getId());
//        String msgOrderTime = String.format("\nVào lúc: %s", order.getOrderDate());
//        String msgTime = "\nThơi gian giao hàng dự kiến từ 3 đến 5 ngày, không tính thứ 7 và chủ nhật.";
//        String msgBody = msgBodyXC + msgBodyCT + msgOrderTime + msgTime;
//        return sendSimpleMail(new EmailDetails(user.getEmail(), msgBody, subject, null));
//    }
//
//    @Override
//    public void sendMailCallOffOrder(User user, String orderID){
//        String subject = "Welcome FESHBOOk!";
//        String msgBodyXC = String.format("Xin chào:  %s", user.getFullname());
//        String msgBodyCT = String.format("\nBạn đã hũy đơn hàng có mã: %s ", orderID);
//        String msgTime = String.format(" vào lúc: %s", new Date());
//        String msgBody = msgBodyXC + msgBodyCT + msgTime;
//        return sendSimpleMail(new EmailDetails(user.getEmail(), msgBody, subject, null));
//    }
//
//    @Override
//    public void sendMailChangePassword(User user){
//        String subject = "Welcome FESHBOOk!";
//        String msgBodyXC = String.format("Xin chào:  %s", user.getFullname());
//        String msgBodyCT = String.format("\nBạn đã thực hiện thay đổi mật khẩu thành công vào lúc %s", new Date());
//        String msgBodyWRN = "\nNếu bạn đó không phải bạn, hay liện hệ với chúng tôi ngay theo email sau: zerodev47@gmail.com";
//        String msgBody = msgBodyXC + msgBodyCT + msgBodyWRN;
//        return sendSimpleMail(new EmailDetails(user.getEmail(), msgBody, subject, null));
//    }
//
//    @Override
//    public void sendMailForgotPassword(User user) {
//        return null;
//    }
}
