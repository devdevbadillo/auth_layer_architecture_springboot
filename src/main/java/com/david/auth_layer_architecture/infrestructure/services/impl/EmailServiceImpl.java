package com.david.auth_layer_architecture.infrestructure.services.impl;

import com.david.auth_layer_architecture.presentation.messages.EmailMessages;
import com.david.auth_layer_architecture.infrestructure.services.interfaces.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    private static final String FROM_EMAIL = "noreply@apptest.com";

    @Value("${uri.frontend}")
    private String frontUri;

    EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailRecoveryAccount(String email, String accessToken){
        String htmlMsg = """
                        <h1>Change Password Request</h1>
                        <p>We received a request to reset your password for your account associated with this email address.</p>

                        <div class="info">
                            <p><strong>Email:</strong> %s</p>
                        </div>
                        <p>To change your password, please click the link below:</p>
                        <ol>
                            <li><a href="%s/auth/change-password?accessToken=%s">Change Your Password</a></li>
                        </ol>
                        <p>If you didn't request a password reset, you can safely ignore this email.</p>

                        <div class="warning">
                            <p><strong>Important:</strong></p>
                            <ul>
                                <li>For security reasons, this link will expire in 10 minutes.</li>
                                <li>Never share this information with anyone.</li>
                            </ul>
                        </div>
                        <p>If you have any questions or need further assistance, feel free to contact our support team.</p>
                """;

        sendEmail(email, accessToken, htmlMsg);
    }

    @Override
    public void sendEmailVerifyAccount(String email, String accessToken, String refreshToken) {
        String htmlMsg = """
                        <h1>Verify Account</h1>
                        <p>We received a request to verify your account for your account associated with this email address.</p>

                        <div class="info">
                            <p><strong>Email:</strong> %s</p>
                        </div>
                        <p>To verify your account, please click the link below:</p>
                        <ol>
                            <li><a href="%s/auth/verify-account?accessToken=%s">Verify Account</a></li>
                            <li>If the link has expired, click here: <a href="%s/auth/refresh-token-to-verify-account?refreshToken=%s">New access</a></li>
                        </ol>
                        <p>If you didn't request a password reset, you can safely ignore this email.</p>

                        <div class="warning">
                            <p><strong>Important:</strong></p>
                            <ul>
                                <li>For security reasons, this link will expire in 1 hour.</li>
                                <li>Never share this information with anyone.</li>
                            </ul>
                        </div>
                        <p>If you have any questions or need further assistance, feel free to contact our support team.</p>
                """;

        sendEmail(email, accessToken, refreshToken, htmlMsg);
    }


    private void sendEmail(String email, String accessToken, String htmlMsg)  {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Change Password Request");
            helper.setText(String.format(htmlMsg, email, frontUri, accessToken), true); // true indicates HTML
            helper.setFrom(FROM_EMAIL);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException(EmailMessages.ERROR_SENDING_EMAIL_MESSAGE);
        }
    }

    private void sendEmail(String email, String accessToken, String refreshToken, String htmlMsg)  {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Verify Account");
            helper.setText(String.format(htmlMsg, email, frontUri, accessToken, frontUri, refreshToken), true); // true indicates HTML
            helper.setFrom(FROM_EMAIL);

            mailSender.send(message);
        } catch (MessagingException | MailSendException e) {
            throw new MailSendException(EmailMessages.ERROR_SENDING_EMAIL_MESSAGE);
        }
    }
}
