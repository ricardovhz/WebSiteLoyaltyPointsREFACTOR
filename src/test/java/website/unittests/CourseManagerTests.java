package website.unittests;

import org.junit.Assert;
import org.junit.Test;
import website.loyaltypoints.service.Course;
import website.loyaltypoints.service.CourseManager;
import website.loyaltypoints.service.Reservation;

public class CourseManagerTests {

    @Test
    public void deveria_criar_um_curso_com_vagas_em_aberto() {

        // Arrange
        CourseManager courseManager = new CourseManager();
        int numeroVagas = 5;
        String nomeCurso = "A-CSD";
        String dataInicio = "28-9-2021";
        String idCurso = "A-CSD-001";

        // Act
        courseManager.createCourse(idCurso, nomeCurso, dataInicio, numeroVagas);

        // Assert
        Course course = courseManager.getCourse(idCurso);
        Assert.assertEquals(numeroVagas, course.getNumberOfSeats());
        Assert.assertEquals(nomeCurso, course.getCourseName());
        Assert.assertEquals(dataInicio, course.getDataInicio());

    }

    @Test
    public void deveria_achar_uma_reserva_pelo_id_do_curso_e_id_reserva() {

        // Arrange
        CourseManager courseManager = new CourseManager();
        int numeroDeVagas = 5;
        String nomeCurso = "A-CSD";
        String dataInicio = "28-9-2021";
        String emailEstudante = "joao@gmail.com";
        String nomeEstudante = "Rafael Melo";
        String idCurso = "A-CSD-001";
        courseManager.createCourse(idCurso, nomeCurso, dataInicio, numeroDeVagas);
        String reservationId = courseManager.createReservation(idCurso, nomeEstudante, emailEstudante);

        // Act
        Reservation reservation = courseManager.getReservationByCourseAndReservationId(idCurso, reservationId);

        // Assert
        Assert.assertEquals(reservation.studentEmail, emailEstudante);
        Assert.assertEquals(reservation.studentName, nomeEstudante);
        Assert.assertEquals(reservation.id, reservationId);
        //TODO verificar data

    }
}
