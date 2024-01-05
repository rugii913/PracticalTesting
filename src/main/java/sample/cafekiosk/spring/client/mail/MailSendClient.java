package sample.cafekiosk.spring.client.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailSendClient {
    public boolean sendEmail(String fromEmail, String toEmail, String subject, String content) {
        // 메일 전송
        log.info("메일 전송");
        throw new IllegalArgumentException("메일 전송");
        // 이렇게 예외를 던지는 것이 실제 메일을 보내는 것이라 가정
        // - mock을 이용해서 실제로 메일이 발송되지 않게(예외가 터지지 않게) 하려고 한다.
    }

    public void a() {
        log.info("a");
    }

    public void b() {
        log.info("b");
    }

    public void c() {
        log.info("c");
    }
}
