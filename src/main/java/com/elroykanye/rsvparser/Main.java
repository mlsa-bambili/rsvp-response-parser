package com.elroykanye.rsvparser;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String fileLocation = "/home/kanye/Downloads/MLSA Program and Intro to MS Dev Tools (RSVP)(1-23) (1).xlsx";

        for (Person person: Driver.excelReader(fileLocation, true)) {
            System.out.println(person);
        }
    }
}
