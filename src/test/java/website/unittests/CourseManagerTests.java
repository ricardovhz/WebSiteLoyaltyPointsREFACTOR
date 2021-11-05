package website.unittests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import website.loyaltypoints.api.CourseDTO;
import website.loyaltypoints.service.Course;
import website.loyaltypoints.service.CourseManager;
import website.loyaltypoints.service.Reservation;

public class CourseManagerTests {

    @Autowired
    private RestTemplate restTemplate;

    String port ="8080";

    final static String SERVER_ADDRESS = "http://localhost:";
    String pathGetCurso = "/api/course/{id}";
    String apiGetCurso = SERVER_ADDRESS + port + pathGetCurso;

    @Test
    public void deveria_criar_um_curso_com_vagas_em_aberto() {

        // Arrange
        int numeroVagas = 5;
        String nomeCurso = "A-CSD";
        String dataInicio = "28-9-2025";
        int courseID;
        CourseDTO courseDTO = new CourseDTO("A-CSD Setembro", "28-9-2021", numeroVagas);
        String pathCreateCourse = "/api/course/create";
        String apiCreateCourse = SERVER_ADDRESS + port + pathCreateCourse;

        // Act
        courseID = restTemplate.postForObject(apiCreateCourse, courseDTO, Integer.class);

        // Assert
        Course course = restTemplate.getForObject(apiGetCurso, Course.class, courseID);
        Assert.assertEquals(numeroVagas, course.getNumberOfSeats());
        Assert.assertEquals(nomeCurso, course.getCourseName());
        Assert.assertEquals(dataInicio, course.getDataInicio());

    }

    @Test
    public void deveria_achar_uma_reserva_pelo_id_do_curso_e_id_reserva() throws Exception {

        // Arrange
        CourseManager courseManager = new CourseManager();
        int numeroDeVagas = 5;
        String nomeCurso = "A-CSD";
        String dataInicio = "28-9-2025";
        String emailEstudante = "joao@gmail.com";
        String nomeEstudante = "Rafael Melo";
        int idCurso = 1;
        courseManager.createCourse(nomeCurso, dataInicio, numeroDeVagas);

        String reservationId = courseManager.createReservation(idCurso, nomeEstudante, emailEstudante);

        // Act
        Reservation reservation = courseManager.getReservationByReservationId(reservationId);

        // Assert
        Assert.assertEquals(reservation.studentEmail, emailEstudante);
        Assert.assertEquals(reservation.studentName, nomeEstudante);
        Assert.assertEquals(reservation.id, reservationId);
        //TODO verificar data

    }
}
