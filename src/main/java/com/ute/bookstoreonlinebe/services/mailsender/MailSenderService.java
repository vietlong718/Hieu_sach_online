package com.ute.bookstoreonlinebe.services.mailsender;

import com.ute.bookstoreonlinebe.entities.Order;
import com.ute.bookstoreonlinebe.entities.User;
import com.ute.bookstoreonlinebe.models.EmailDetails;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface MailSenderService {

    void sendMailSignup(User user);

    void sendMailNewOrder(User user, Order order);

    void sendMailCallOffOrder(User user, Order order);

    void sendMailChangePassword(User user);

    void sendMailForgotPassword(User user);
}
