package website;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import website.loyaltypoints.email.EmailSender;

@Configuration
public class EmailSenderConfiguration {

  @Bean
  @Profile("bdd")
  public EmailSender emailSender() {
    return Mockito.mock(EmailSender.class);
  }
}
