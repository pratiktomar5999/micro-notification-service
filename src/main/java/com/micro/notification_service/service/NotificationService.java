package com.micro.notification_service.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.micro.notification_service.event.OrderEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;
    
    @KafkaListener(topics="order-placed")
    public void listen(OrderEvent order){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("pratiktomar@gmail.com");
            messageHelper.setTo(order.getEmail());
            messageHelper.setSubject(String.format("Your Order with order number %s is placed successfully", order.getOrderNumber()));
            messageHelper.setText(String.format("""
                    Hi,

                    Your order with order number %s is placed successfully.

                    Best Regards,
                    pratik Tomar
                    """, order.getOrderNumber()));
        };

        try{
            javaMailSender.send(messagePreparator);
        }catch(MailException e){
            throw new RuntimeException("Exception Occurred while sending mail -" + e.getMessage());
        }
    }
}
