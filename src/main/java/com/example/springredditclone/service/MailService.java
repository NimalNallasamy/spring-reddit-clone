package com.example.springredditclone.service;

import com.example.springredditclone.entity.NotificationEmail;
import com.example.springredditclone.exception.SpringRedditException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;
    /**
     * This method is used to send the mail to the customer.
     * This method makes use of MimeMessageHelper class to send the mail.
     */
    @Async
    public void sendMail(NotificationEmail notificationEmail){

        // Setting the data of the mail.
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("springreddit@email.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };

        try{
            mailSender.send(messagePreparator);
            log.info("Activation Email Sent!");
//            System.out.println("Activation Email Sent!");
        }catch(Exception ex){
//            System.out.println("Exception while sending the activation email! "+ex);
             throw new SpringRedditException("Exception occured while sending the mail to customer."+ex);
        }
    }

}
