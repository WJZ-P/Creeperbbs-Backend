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
        emailService.sendEmail("1798748@qq.com", "测试邮件", "这是一封测试邮件");
    }

}
