package com.elroykanye.mailer.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sender {
    private String sendingEmail;
    private String fromEmail;
    private String password;
}
