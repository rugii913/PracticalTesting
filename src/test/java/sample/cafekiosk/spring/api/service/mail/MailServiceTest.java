package sample.cafekiosk.spring.api.service.mail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    // @Spy
    @Mock
    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks // 테스트 대상인 mailService를 제어 - 위 @Mock 객체들을 알아서 주입 받는다.
    private MailService mailService;
    
    @DisplayName("메일 전송 테스트")
    @Test
    void sendMail() {
        // given
        // MailService에서 사용하는 2개의 빈을 mocking 해줘야 한다.
//        @Mock, @ExtendWith(MockitoExtension.class)를 사용하지 않은 경우
//        MailSendClient mailSendClient = Mockito.mock(MailSendClient.class);
//        MailSendHistoryRepository mailSendHistoryRepository = Mockito.mock(MailSendHistoryRepository.class);

        // MailService mailService = new MailService(mailSendClient, mailSendHistoryRepository);

        /*Mockito.when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true); // BDD 상으로는 given인데, 호출하는 함수는 when이라서 뭔가 어색하다*/
        
        // @Spy - mocking 대상 메서드 중 일부 기능만 바꾸고 나머지는 그대로 살리고 싶은 경우
        // → 한 객체가 여러 기능이 있는데, 한 기능만 stubbing 하고 싶은 경우
        // @Spy는 stubbing이 되지 않으므로 when()을 사용할 수 없다. → do~ 메서드들 사용
        /*doReturn(true)
                .when(mailSendClient)
                .sendEmail(anyString(), anyString(), anyString(), anyString());*/
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        // when
        boolean result = mailService.sendMail("", "", "", "");

        // then
        assertThat(result).isTrue();
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

    }
}
