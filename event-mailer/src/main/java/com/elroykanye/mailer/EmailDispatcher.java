package com.elroykanye.mailer;

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
            .password("55Brocoli!!")
            .build();

    private void setupProperties() {
        String MAIL_HOST = "smtp-mail.outlook.com";
        String MAIL_PORT = "587";


        PROPERTIES.put("mail.smtp.user", SENDER.getSendingEmail());
        PROPERTIES.put("mail.smtp.password", SENDER.getPassword());


        PROPERTIES.put("mail.smtp.host", MAIL_HOST);
        PROPERTIES.put("mail.smtp.port", MAIL_PORT);
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
        PROPERTIES.put("mail.smtp.starttls.required", "true");
        PROPERTIES.put("mail.smtp.ssl.protocols", "TLSv1.2");
        PROPERTIES.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


        // System.out.println("Enter password for: " + PROPERTIES);
        // String password = new Scanner(System.in).nextLine();

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
            messageBody2.setFileName(attachment.getAttachmentPathDocx());

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
