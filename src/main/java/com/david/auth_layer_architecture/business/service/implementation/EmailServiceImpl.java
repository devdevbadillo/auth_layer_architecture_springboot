package com.david.auth_layer_architecture.business.service.implementation;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.david.auth_layer_architecture.business.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmailRecoveryAccount(String email, String accessToken) throws MessagingException{
        String htmlMsg = """
                        <h1>Change Password Request</h1>
                        <p>We received a request to reset your password for your account associated with this email address.</p>

                        <div class="info">
                            <p><strong>Email:</strong> %s</p>
                        </div>
                        <p>To change your password, please click the link below:</p>
                        <ol>
                            <li><a href="http://localhost:4200/auth/change-password?accessToken=%s">Change Your Password</a></li>
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

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Change Password Request");
            helper.setText(String.format(htmlMsg, email, accessToken), true); // true indicates HTML
            helper.setFrom("noreply@apptest.com");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Error sending email");
        }
    }
}
