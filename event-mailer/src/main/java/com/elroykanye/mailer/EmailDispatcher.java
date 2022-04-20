package com.elroykanye.mailer;

import com.elroykanye.mailer.models.Attachment;
import com.elroykanye.mailer.models.Attendee;
import com.elroykanye.mailer.models.EventDetails;
import com.elroykanye.mailer.models.Sender;
import org.apache.commons.mail.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import static com.elroykanye.mailer.Util.getAttachment;

public class EmailDispatcher {

    private final Properties PROPERTIES = new Properties();
    private final Sender SENDER  = Sender.builder()
            .sendingEmail("elroykanye@outlook.com")
            .fromEmail("elroykanye@outlook.com")
            .password("")
            .build();
    String MAIL_HOST = "smtp-mail.outlook.com";
    String MAIL_PORT = "587";

    /**
     * Modifies the PROPERTIES object to set up the mail server and addresses
     */
    private void setupProperties() {



        PROPERTIES.put("mail.smtp.user", SENDER.getSendingEmail());
        PROPERTIES.put("mail.smtp.password", SENDER.getPassword());


        PROPERTIES.put("mail.smtp.host", MAIL_HOST);
        PROPERTIES.put("mail.smtp.port", MAIL_PORT);
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.starttls.required", "true");
        PROPERTIES.put("mail.smtp.ssl.protocols", "TLSv1.2");
        PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    /**
     * Dispatches an email to the attendee with an attached certificate
     * @param eventDetails: represents the event information to be included in the email
     * @param attendee: information about the attendee including name and email
     */
    public void sendEmailCommons(EventDetails eventDetails, Attendee attendee, String password) {
        SENDER.setPassword(password);

        setupProperties();
        Attachment fileAttachment = Util.getAttachment(attendee);
        EmailAttachment emailAttachment = new EmailAttachment();
        emailAttachment.setPath(fileAttachment.getAttachmentPathPdf());
        emailAttachment.setDisposition(EmailAttachment.ATTACHMENT);
        emailAttachment.setName(fileAttachment.getAttachmentName());
        emailAttachment.setDescription(attendee.getName() +
                " " +
                eventDetails.getEventName() +
                " attendance certificate.");


        try {
            // create the email message
            MultiPartEmail multiPartEmail = new MultiPartEmail();
            multiPartEmail.setHostName(MAIL_HOST);
            multiPartEmail.setSmtpPort(587);
            multiPartEmail.setStartTLSEnabled(true);
            multiPartEmail.setAuthentication(SENDER.getSendingEmail(), SENDER.getPassword());
            multiPartEmail.setBoolHasAttachments(true);
            multiPartEmail.setSSLOnConnect(true);
            multiPartEmail.setStartTLSRequired(true);
            multiPartEmail.setMailSession(Session.getDefaultInstance(PROPERTIES));
            // TODO change this to attendee email

            multiPartEmail.addTo(attendee.getEmail());



            multiPartEmail.setFrom(SENDER.getFromEmail());
            multiPartEmail.setSubject(eventDetails.getEventSubject());
            multiPartEmail.setMsg(eventDetails.getEventDesc());

            multiPartEmail.attach(emailAttachment);
            multiPartEmail.send();

            System.out.println("Mail successfully sent to: " + attendee.getEmail());
        } catch (EmailException e) {
            e.printStackTrace();
        }

    }

    public void sendEmail(EventDetails eventDetails, Attendee attendee) {
        setupProperties();
        Session session = Session.getDefaultInstance(PROPERTIES);

        try {
            MimeMessage message = new MimeMessage(session);

            // set from
            message.setFrom(new InternetAddress(SENDER.getFromEmail()));

            // set recipient
            // TODO change this recipient
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("elroyksocial@gmail.com"));

            // set subject
            message.setSubject(eventDetails.getEventName());

            // creating first body part
            BodyPart messageBody1 = new MimeBodyPart();
            messageBody1.setText(eventDetails.getEventDesc() + "\n" + eventDetails.getEventAppreciation());

            // creating second part
            BodyPart messageBody2 = new MimeBodyPart();
            Attachment attachment = getAttachment(attendee);
            assert attachment != null;
            DataSource dataSource = new FileDataSource(attachment.getAttachmentPathDocx());
            messageBody2.setDataHandler(new DataHandler(dataSource));
            messageBody2.setFileName(attachment.getAttachmentName());

            // creating multipart object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody1);
            multipart.addBodyPart(messageBody2);


            // set body of the email
            message.setContent(multipart);

            // send email
            //Transport.send(message);
            Transport transport = session.getTransport("smtp");
            transport.connect(SENDER.getSendingEmail(), SENDER.getPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Mail successfully sent to: " + attendee.getEmail());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}
