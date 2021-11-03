package website.loyaltypoints.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

    public class CourseManager {

    private final Map<Integer, Course> mapCourses;

    private static final Logger LOG = LoggerFactory.getLogger(CourseManager.class);

    public CourseManager() {
        mapCourses = new HashMap<Integer, Course>();
    }

    private String DB_URL = "jdbc:h2:mem:testdbs";
    private String USER = "sa";
    private String PASS = "";

    public int createCourse(String courseName, String courseDate, int numberOfSeats) {

        Course novoCurso = new Course(courseName, courseDate, numberOfSeats);
        int courseId;
        String sql = "INSERT INTO TBL_COURSES(courseName, courseDate, numberOfSeats) VALUES (?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            LOG.debug("Inserting records into the table...");
            statement.setString(1, courseName);
            statement.setString(2, courseDate);
            statement.setInt(3, numberOfSeats);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String responseId = String.valueOf(generatedKeys.getLong(1));
                    courseId = Integer.parseInt(responseId);
                    novoCurso.setId(courseId);
                } else {
                    throw new RuntimeException("Creating reservation failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Creating reservation failed, no ID obtained.", e);
        }

        mapCourses.put(courseId, novoCurso);
        return courseId;
    }

    public Course getCourse(int codigoCurso) {
        LOG.debug("Requesting course: " + codigoCurso);

        Course novoCurso = new Course();
        int courseId;

        String sql = String.format("SELECT id, courseName, courseDate, numberOfSeats FROM TBL_COURSES WHERE id = '%s'", codigoCurso);

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {

                novoCurso = new Course(rs.getString("courseName"), rs.getString("courseDate"), rs.getInt("numberOfSeats"));
                novoCurso.setId(rs.getInt("id"));

                LOG.debug("ID: " + rs.getInt("id"));
                LOG.debug(", courseName: " + rs.getString("courseName"));
                LOG.debug(", courseDate: " + rs.getString("courseDate"));
                LOG.debug(", numberOfSeats: " + rs.getInt("numberOfSeats"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Creating course failed: ", e);
        }

        return novoCurso;
    }

    public String createReservation(int courseId, String nomeEstudante, String emailEstudante) throws Exception {

        Course course = mapCourses.get(courseId);
        String reservationId = "";

        if (course.getNumberOfSeats() <= 0) {
            throw new CursoNaoPossueVagasException();
        } else {

            String sql = "INSERT INTO TBL_RESERVATIONS(studentName,studentEmail,courseId,reservationDate) VALUES (?, ?, ?, ?)";

            try (
                    Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ) {
                LOG.debug("Inserting records into the table...");
                statement.setString(1, nomeEstudante);
                statement.setString(2, emailEstudante);
                statement.setInt(3, course.courseId);
                statement.setString(4, String.valueOf(java.time.LocalDate.now()));

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reservationId = String.valueOf(generatedKeys.getLong(1));
                    } else {
                        throw new RuntimeException("Creating reservation failed, no ID obtained.");
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException("Creating reservation failed, no ID obtained.", e);
            }

            course.createReservation(nomeEstudante, emailEstudante);
            return reservationId;
        }

        // Formatar email para admin
        // Formatar email para cliente

        // Enviar email

    }

    public int getNumberOfSeats(String courseId) {
        return mapCourses.get(courseId).getNumberOfSeats();
    }

    public String getDataInicio(String courseId) {
        return mapCourses.get(courseId).getDataInicio();
    }


    public Reservation getReservationByReservationId(String reservationId) {
        String sql = String.format("SELECT id, studentName, studentEmail, courseId, reservationDate FROM TBL_RESERVATIONS WHERE id = '%s'", reservationId);
        Reservation reservation = new Reservation();

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            while (rs.next()) {
                String responseId = String.valueOf(rs.getInt("id"));
                reservation = new Reservation(rs.getString("studentEmail"), rs.getString("studentName"));
                reservation.setId(responseId);

                LOG.debug("ID: " + rs.getInt("id"));
                LOG.debug(", studentEmail: " + rs.getString("studentEmail"));
                LOG.debug(", studentName: " + rs.getString("studentName"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Creating reservation failed: ", e);
        }
        return reservation;
    }

}
