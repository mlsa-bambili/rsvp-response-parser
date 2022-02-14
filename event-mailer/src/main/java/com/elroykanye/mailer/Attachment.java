package com.elroykanye.mailer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Attachment {
    private String attachmentName;
    private String attachmentPathDocx;
    private String attachmentPathPdf;
}
