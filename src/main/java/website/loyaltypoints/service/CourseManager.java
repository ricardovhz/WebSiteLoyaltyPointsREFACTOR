package website.loyaltypoints.service;

import java.util.HashMap;
import java.util.Map;

public class CourseManager {

    private final Map<String, Course> mapCourses;

    public CourseManager() {
        mapCourses = new HashMap<String, Course>();
    }

    public void createCourse(String courseId, String nomeCurso, String dataInicio, int numeroDeVagas) {
        Course novoCurso = new Course(courseId, nomeCurso, dataInicio, numeroDeVagas);
        mapCourses.put(courseId, novoCurso);
    }

    public Course getCourse(String codigoCurso) {
        return mapCourses.get(codigoCurso);
    }

    public String createReservation(String courseId, String nomeEstudante, String emailEstudante) {
        Course course = mapCourses.get(courseId);
        return course.createReservation(nomeEstudante, emailEstudante);
    }

    public int getNumberOfSeats(String courseId) {
        return mapCourses.get(courseId).getNumberOfSeats();
    }

    public String getDataInicio(String courseId) {
        return mapCourses.get(courseId).getDataInicio();
    }


    public Reservation getReservationByCourseAndReservationId(String courseId, String reservationId) {
        return mapCourses.get(courseId).getReservation(reservationId);
    }


}
