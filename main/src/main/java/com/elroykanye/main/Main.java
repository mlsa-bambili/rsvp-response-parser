package com.elroykanye.main;

import com.elroykanye.mailer.models.Attendee;
import com.elroykanye.mailer.CertGenerator;
import com.elroykanye.mailer.EmailDispatcher;
import com.elroykanye.mailer.models.EventDetails;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileLocation = "/home/kanye/IdeaProjects/rsvp-parser/event-rsvp-parser/src/main/resources/MLSA Program and Intro to MS Dev Tools (RSVP)(1-96).xlsx";
        String eventName = "Introduction to the Microsoft Learn Student Ambassadors Program and Microsoft Developer Technologies";

        CertGenerator certGenerator = new CertGenerator();
        List<Attendee> attendees = certGenerator.generateCerts(eventName, fileLocation);

        EmailDispatcher emailDispatcher = new EmailDispatcher();

        EventDetails eventDetails = EventDetails.builder()
                .eventName(eventName)
                .eventSubject("Microsoft Learn Event Certificate")
                .eventDesc(
                        "This certificate shows that you attended the Introduction to Microsoft Learn Student Ambassadors Program and Introduction to " +
                                "Microsoft Developer Technologies Event. \n" +
                                "You can share this with your network and invite others to the next event hosted by " +
                                "Microsoft Learn Student Ambassador.\n\n\n" +
                                "" +
                                "Thank you for taking part in this event.\n\n\n" +
                                "This email and attachment was generated using https://github.com/mlsa-bambili/rsvp-response-parser\n" +
                                "You can contribute to making this tool even better by collaborating on GitHub."
                )
                .eventAppreciation("Thank you for taking part in this event.\n\n\n" +
                        "This email and attachment was generated using https://github.com/mlsa-bambili/rsvp-response-parser\n" +
                        "You can contribute to making this tool even better by collaborating on GitHub.").build();

        System.out.println("Enter the password for the sending account: ");
        String pass = new Scanner(System.in).nextLine();
        System.out.println("Starting...");

        attendees.forEach(attendee -> {
            System.out.println(attendee);
            emailDispatcher.sendEmailCommons(eventDetails, attendee, pass);
        });
        System.out.println("Stopped!");
    }
}
