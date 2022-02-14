package com.elroykanye.main;

import com.elroykanye.mailer.Attendee;
import com.elroykanye.mailer.CertGenerator;
import com.elroykanye.mailer.EmailDispatcher;
import com.elroykanye.mailer.EventDetails;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String fileLocation = "/home/kanye/Downloads/MLSA Program and Intro to MS Dev Tools (RSVP)(1-23) (1).xlsx";
        String eventName = "Test Event";
        CertGenerator certGenerator = new CertGenerator();
        List<Attendee> attendees = certGenerator.generateCerts(eventName, fileLocation);

        EmailDispatcher emailDispatcher = new EmailDispatcher();

        Attendee elroy = attendees.get(0);
        EventDetails eventDetails = EventDetails.builder()
                .eventName(eventName)
                .eventDesc("This is a sample event description")
                .eventAppreciation("Thank you for attending this event").build();
        emailDispatcher.sendEmail(eventDetails, elroy);
    }
}
