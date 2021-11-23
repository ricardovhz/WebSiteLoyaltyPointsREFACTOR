package website.loyaltypoints.email;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
@Profile("production")
public class EmailSender {
  private static final Logger LOG = LoggerFactory.getLogger(EmailSender.class);

  public void sendEmail(
      String to, String subject, String templatePath, Map<String, Object> templateContext) {
    sendEmail("contato@working-agile.com", to, subject, templatePath, templateContext);
  }

  public void sendEmailToAdmin(
      String subject, String templatePath, Map<String, Object> templateContext) {
    sendEmail(
        "noreply@working-agile.com", "axelberle@gmail.com", subject, templatePath, templateContext);
  }

  private JavaMailSenderImpl configureEmailSender() {
    String host = "email-ssl.com.br";
    String username = "no-reply@working-agile.com";
    String password = "segredo!";
    String protocol = "smtps";
    int port = 465;
    String auth = "true";
    String ttlsEnabled = "true";
    String connectiontimeout = "2000";

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

  private void sendEmail(
      String from,
      String to,
      String subject,
      String templatePath,
      Map<String, Object> templateContext) {

    JavaMailSenderImpl mailSender = configureEmailSender();

    try {
      MimeMessage message = mailSender.createMimeMessage();

      message.setFrom(new InternetAddress(from));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
      message.setSubject(subject, StandardCharsets.UTF_8.toString());
      String velocityHtml = processTemplate(templatePath, templateContext);
      message.setContent(velocityHtml, "text/html");

      LOG.info("sending email to " + to + " and subject [" + subject + "]");
      mailSender.send(message);
    } catch (Exception e) {
      LOG.debug("failed sending reply email", e);
    }
  }

  private String processTemplate(String templatePath, Map<String, Object> templateContext) {
    VelocityContext context = new VelocityContext();
    templateContext.forEach(context::put);

    Template template = Velocity.getTemplate(templatePath);
    StringWriter writer = new StringWriter();
    template.merge(context, writer);
    return writer.toString();
  }
}
