package website.stepdefinitions.courseenrollment;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import java.io.IOException;
import java.util.Arrays;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;
import website.loyaltypoints.api.CourseDTO;
import website.loyaltypoints.api.NewReservationRequestDTO;
import website.loyaltypoints.api.NewReservationResponseDTO;
import website.loyaltypoints.service.Course;
import website.loyaltypoints.service.Reservation;

public class CourseEnrollmentSteps {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @LocalServerPort
    String port;

    final static String SERVER_ADDRESS = "http://localhost:";

    String studentName;
    String studentEmail;
    int courseID;
    String reservationId;
    boolean serverError;

    @Dado("um estudante que quer participar de um curso")
    public void um_estudante_que_quer_participar_de_um_curso() {
        studentName = "João";
        studentEmail = "mellotario@gmail.com";
    }

    @Dado("o curso tem ainda {int} vagas em aberto")
    public void o_curso_tem_ainda_vagas_em_aberto(int vagasEmAberto) {

        String pathCreateCourse = "/api/course/create";
        String apiCreateCourse = SERVER_ADDRESS + port + pathCreateCourse;

        CourseDTO courseDTO = new CourseDTO("A-CSD Setembro", "28-9-2021", vagasEmAberto);

        courseID = restTemplate.postForObject(apiCreateCourse, courseDTO, Integer.class);
    }

    @Quando("o estudante reserva sua vaga")
    public void o_estudante_reserva_sua_vaga() {
       String pathCourseReserve = "/api/course/reserve";
       String apiCourseReserve = SERVER_ADDRESS + port + pathCourseReserve;

        NewReservationRequestDTO reservationDTO = new NewReservationRequestDTO(courseID, studentName, studentEmail);

        try {
            NewReservationResponseDTO responseDTO = restTemplate.postForObject(apiCourseReserve, reservationDTO, NewReservationResponseDTO.class);
            reservationId = responseDTO.reservationId;
            Assert.assertNotNull(reservationId);
        } catch (Exception e) {
            serverError = true;
        }
    }

    @Então("a vaga deveria estar marcada para esperando pagamento")
    public void a_vaga_deveria_ser_marcada_para_esperando_pagamento() {

        String pathReservation = "/api/course/reservation/{reservationId}";
        String apiReservation = SERVER_ADDRESS + port + pathReservation;

        Reservation reservation = restTemplate.getForObject(apiReservation, Reservation.class, reservationId);

        Assertions.assertEquals(reservationId, reservation.id);

    }

    @Então("o curso deveria ter somente {int} vagas em aberto")
    public void o_curso_deveria_ter_somente_vagas_em_aberto(int expectedNunberOfSeats) {

        String pathGetCurso = "/api/course/{id}";
        String apiGetCurso = SERVER_ADDRESS + port + pathGetCurso;

        Course course = restTemplate.getForObject(apiGetCurso, Course.class, courseID);

        Assert.assertNotNull(course);
        Assertions.assertEquals(expectedNunberOfSeats, course.getNumberOfSeats());
    }

    @Dado("o curso nao tem vagas em aberto")
    public void o_curso_nao_tem_vagas_em_aberto() {
        String pathCreateCourse = "/api/course/create";
        String apiCreateCourse = SERVER_ADDRESS + port + pathCreateCourse;

        CourseDTO courseDTO = new CourseDTO("A-CSD Setembro", "28-9-2021", 0);

        courseID = restTemplate.postForObject(apiCreateCourse, courseDTO, Integer.class);
    }

    @Então("o estudante nao deve estar reservado")
    public void o_estudando_nao_deve_estar_reservado() {
        Assertions.assertTrue(this.serverError);
    }

  @Então("email para estudante é enviado com assunto {string}")
  public void email_para_estudante_é_enviado_com_assunto(String string) {
    verify(javaMailSender)
        .send(
            argThat(
                (MimeMessage argument) -> {
                  try {
                    boolean d =
                        argument.getRecipients(RecipientType.TO)[0].toString().equals(studentEmail)
                            && argument
                                .getFrom()[0]
                                .toString()
                                .equals("contato@working-agile.com")
                            && argument.getSubject().equals(string)
                            && argument.getContent() != null;
                    if (d) {
                      System.out.println(
                          argument.getRecipients(RecipientType.TO)[0].toString()
                              + Arrays.toString(argument.getAllRecipients())
                              + argument.getSubject()
                              + argument.getContent());
                    }
                    return d;
                  } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                  }
                  return false;
                }));
  }

  @Então("email de copia para admin é enviado com assunto {string}")
  public void email_de_copia_para_admin_é_enviado_com_assunto(String string) {
    verify(javaMailSender).send(argThat((MimeMessage argument) -> {
      try {
        return argument.getRecipients(RecipientType.TO)[0].toString().equals("axelberle@gmail.com") &&
            argument.getFrom()[0].toString().equals("noreply@working-agile.com") &&
            argument.getSubject().equals(string) &&
            argument.getContent() != null;
      } catch (MessagingException | IOException e) {
        e.printStackTrace();
      }
      return false;
    }));
    }

  @Então("email para admin é enviado com assunto {string}")
  public void email_para_admin_é_enviado_com_assunto(String string) {
    verify(javaMailSender).send(argThat((MimeMessage argument) -> {
      try {
        return argument.getRecipients(RecipientType.TO)[0].toString().equals("axelberle@gmail.com") &&
            argument.getFrom()[0].toString().equals("noreply@working-agile.com") &&
            argument.getSubject().equals(string) &&
            argument.getContent() != null;
      } catch (MessagingException | IOException e) {
        e.printStackTrace();
      }
      return false;
    }));
  }

}
