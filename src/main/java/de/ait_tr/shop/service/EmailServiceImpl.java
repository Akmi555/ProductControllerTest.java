package de.ait_tr.shop.service;

import de.ait_tr.shop.model.entity.User;
import de.ait_tr.shop.service.interfaces.ConfirmationCodeService;
import de.ait_tr.shop.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Configuration mailConfiguration;
    private final ConfirmationCodeService codeService;

    private final static String HOST = "http://localhost:8080/api";

    public EmailServiceImpl(JavaMailSender mailSender, Configuration mailConfiguration, ConfirmationCodeService codeService) {
        this.mailSender = mailSender;
        this.mailConfiguration = mailConfiguration;
        this.codeService = codeService;

        // Настройка кодировки
        mailConfiguration.setDefaultEncoding("UTF-8");
        // указываем папку с шаблонами
        mailConfiguration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/mail"));
    }


    @Override
    public void sendConfirmationEmail(User user) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String emailText = generateEmailText(user);

            // Получим адрес отправителя из переменной среды
            String fromAddress = System.getenv("MAIL_USERNAME");
            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail());
            helper.setSubject("Registration Confirmation");
            helper.setText(emailText, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateEmailText(User user){

        try {
            // Загрузка шаблона
            Template template = mailConfiguration.getTemplate("confirm_reg_mail.ftlh");

            // Генерация кода подтверждения
            String code = codeService.generationConfirmationCode(user);

            // http://localhost:8080/confirm?code=значение_кода
            String confirmationLink = HOST + "/confirm?code=" + code;

            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getUserName());
            model.put("confirmationLink", confirmationLink);

            // Генерирую текст письма
            String emailText = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            return emailText;

        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }

    }
}
