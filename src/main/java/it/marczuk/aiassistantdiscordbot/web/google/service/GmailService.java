package it.marczuk.aiassistantdiscordbot.web.google.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailService {

    private final Gmail gmailService;

    public void sendEmail(String to, String from, String subject, String bodyText) {
        MimeMessage email = createEmail(to, from, subject, bodyText);
        try {
            sendMessage(gmailService, "me", email);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        try {
            email.setFrom(new InternetAddress(from));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            email.setText(bodyText);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return email;
    }

    private Message createMessageWithEmail(MimeMessage email) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            email.writeTo(buffer);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private void sendMessage(Gmail service, String userId, MimeMessage email) throws IOException {
        Message message = createMessageWithEmail(email);
        service.users().messages().send(userId, message).execute();
    }
}
