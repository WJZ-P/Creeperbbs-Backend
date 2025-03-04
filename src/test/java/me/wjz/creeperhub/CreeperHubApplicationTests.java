package me.wjz.creeperhub;

import me.wjz.creeperhub.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CreeperHubApplicationTests {
    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
    }

}
