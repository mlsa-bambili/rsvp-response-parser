package com.elroykanye.mailer;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

public class Util {
    private final static String certPathDoc = "event-mailer/src/main/resources/doc/";
    private final static String certPathPdf = "event-mailer/src/main/resources/pdf/";

    final static String templateDoc = "event-mailer/src/main/resources/EventCertificateTemplate.docx";
    static final String senderEmail = "elroykanye@outlook.com";

    public static Attachment getAttachment(Attendee attendee) {
        String attendeeEmail = attendee.getEmail();
        attendeeEmail = attendeeEmail.toLowerCase().split("@")[0];

        return Attachment.builder()
                .attachmentName(attendeeEmail)
                .attachmentPathDocx(certPathDoc + attendeeEmail + ".docx")
                .attachmentPathPdf(certPathPdf + attendeeEmail + ".pdf")
                .build();
    }

    static void saveAsPdf(Document certificate, Attachment attachment) throws Exception {
        certificate.save(attachment.getAttachmentPathPdf(), SaveFormat.PDF);
    }
}
