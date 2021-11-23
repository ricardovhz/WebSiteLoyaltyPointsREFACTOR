package website;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Profile("production")
public class ProdJavaMailSenderConfiguration {

  @Value("${mail.host}")
  private String host;

  @Value("${mail.username}")
  private String username;

  @Value("${mail.password}")
  private String password;

  @Value("${mail.protocol}")
  private String protocol;

  @Value("${mail.port}")
  private int port;

  @Value("${mail.auth:true}")
  private String auth = "true";

  @Value("${mail.ttls:true}")
  private String ttlsEnabled = "true";

  @Value("${mail.timeout:2000}")
  private String connectiontimeout = "2000";

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(host);
    mailSender.setUsername(username);
    mailSender.setPassword(password);
    mailSender.setProtocol(protocol);
    mailSender.setPort(port);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", protocol);
    props.put("mail.smtp.auth", auth);
    props.put("mail.smtp.starttls.enable", ttlsEnabled);
    props.put("mail.smtp.connectiontimeout", connectiontimeout);
    props.put("mail.debug", true);

    return mailSender;
  }
}
