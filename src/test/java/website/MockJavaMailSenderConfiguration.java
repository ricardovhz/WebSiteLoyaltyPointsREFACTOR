package website;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MockJavaMailSenderConfiguration {

  @Bean
  public JavaMailSender mockJavaMailSender() {
    JavaMailSender mock = Mockito.mock(JavaMailSender.class);
    Mockito.when(mock.createMimeMessage()).then(a -> new MimeMessage((Session) null));
    return mock;
  }
}
