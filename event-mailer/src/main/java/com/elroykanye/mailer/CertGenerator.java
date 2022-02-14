package com.elroykanye.mailer;


import com.deepoove.poi.XWPFTemplate;
import com.elroykanye.parser.Driver;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertGenerator {
    List<Attendee> attendees = new ArrayList<>();

    public List<Attendee> generateCerts(String eventName, String attendeeList) {
        getAttendees(attendeeList);
        attendees.forEach(attendee -> {
            try {
                generateCertDocx(eventName, attendee);
                attendee.setCertificate(Util.getAttachment(attendee).getAttachmentPathPdf());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return attendees;
    }

    private void getAttendees (String attendeeList) {
        Driver.excelReader(attendeeList, true)
                .forEach(person -> {
                    attendees.add(Attendee.builder()
                            .name(person.getName())
                            .email(person.getEmail())
                            .build());
                });
    }

    private void generateCertDocx(String eventName, Attendee attendee) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("attendee", attendee.getName());
        data.put("event", eventName);

        XWPFTemplate.compile(Util.templateDoc)
                .render(data)
                .writeToFile(Util.getAttachment(attendee).getAttachmentPathDocx());
    }

    private void generateCertPdf(Attendee attendee) {
        // open the Word document
        Attachment certAttachment = Util.getAttachment(attendee);

        try (InputStream inputStream = new FileInputStream(certAttachment.getAttachmentPathDocx()) ;
             OutputStream outputStream = new FileOutputStream(certAttachment.getAttachmentPathPdf())) {

            XWPFDocument xwpfDocument = new XWPFDocument(inputStream);

            // create the pdf options
            PdfOptions pdfOptions = PdfOptions.create();

            // convert doc to pdf
            PdfConverter.getInstance().convert(xwpfDocument, outputStream, pdfOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
