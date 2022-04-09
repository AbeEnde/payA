package com.mycompany.myapp.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mycompany.myapp.domain.Pay;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class MessageHandling {

    @MessageMapping("/ws")
    @SendTo("/queue")
    public Pay greeting(Pay message) throws InterruptedException {
        Thread.sleep(1000);

        return message;
    }
}
