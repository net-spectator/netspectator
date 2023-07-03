package com.example.notificationservice.Controller;

import com.example.notificationservice.service.impl.EmailSenderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class EmailController {

    private final EmailSenderServiceImpl emailService;
    //test
    @PostMapping("/{to}")
    public void sendEmail(@PathVariable String to) {
        emailService.sendEmail(to);
    }
}
