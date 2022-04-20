package com.elroykanye.mailer.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Attendee {
    private String name;
    private String email;
    private String eventName;
    private String certificate;
}
