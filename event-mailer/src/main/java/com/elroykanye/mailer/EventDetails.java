package com.elroykanye.mailer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDetails {
    private String eventName;
    private String eventDesc;
    private String eventSubject;
    private String eventAppreciation;
}
