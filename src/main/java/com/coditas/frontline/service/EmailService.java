package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
