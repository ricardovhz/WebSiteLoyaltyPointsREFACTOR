package website.loyaltypoints.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import website.loyaltypoints.email.EmailSender;
import website.loyaltypoints.repository.CourseRepository;

@Service
public class CourseManager {
    private static final Logger LOG = LoggerFactory.getLogger(CourseManager.class);
    private static final String VELOCITY_TEMPLATE_REPLY = "/email-templates/email-reservation-reply.vm";
    private static final String VELOCITY_TEMPLATE_ADMIN_COPY = "/email-templates/email-reservation-copy-to-admin.vm";
    private static final String VELOCITY_TEMPLATE_ADMIN_COURSE_FULL = "/email-templates/email-course-full.vm";

    private final Map<Integer, Course> mapCourses;
    private String DB_URL = "jdbc:h2:mem:testdbs";
    private String USER = "sa";
    private String PASS = "";
    int numberOfSeats;
    int courseId;

    private final CourseRepository courseRepository;
    private final EmailSender emailSender;

    public CourseManager(CourseRepository courseRepository, EmailSender emailSender) {
        mapCourses = new HashMap<>();
        this.courseRepository = courseRepository;
        this.emailSender = emailSender;
    }

    public int createCourse(String courseName, String courseDate, int numberOfSeats) {

        Course novoCurso = new Course(courseName, courseDate, numberOfSeats);
        this.numberOfSeats = numberOfSeats;
        String sql = "INSERT INTO TBL_COURSES(courseName, courseDate, numberOfSeats) VALUES (?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            LOG.debug("Inserting records into the table...");
            statement.setString(1, courseName);
            statement.setString(2, courseDate);
            statement.setInt(3, numberOfSeats);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    String responseId = String.valueOf(generatedKeys.getLong(1));
                    this.courseId = Integer.parseInt(responseId);
                    novoCurso.setId(this.courseId);
                } else {
                    throw new RuntimeException("Creating course failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Creating course failed, connection failed", e);
        }

        return this.courseId;
    }

    public Course getCourse(int codigoCurso) {

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = connection.createStatement();
        ) {
            stmt.setMaxRows(1);
            String sql = String.format("SELECT id, courseName, courseDate, numberOfSeats FROM TBL_COURSES WHERE id = '%s'", codigoCurso);
            LOG.debug("Requesting course: " + codigoCurso);
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                Course novoCurso = new Course(rs.getString("courseName"), rs.getString("courseDate"), rs.getInt("numberOfSeats"));
                novoCurso.setId(rs.getInt("id"));
                this.numberOfSeats = rs.getInt("numberOfSeats");

                LOG.debug("ID: " + rs.getInt("id"));
                LOG.debug(", courseName: " + rs.getString("courseName"));
                LOG.debug(", courseDate: " + rs.getString("courseDate"));
                LOG.debug(", numberOfSeats: " + numberOfSeats);
                return novoCurso;
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Getting course failed: ", e);
        }
    }

    public String createReservation(int courseId, String nomeEstudante, String emailEstudante) throws Exception {
        if (this.numberOfSeats <= 0) {
            emailSender.sendEmailToAdmin("Curso sem vagas", VELOCITY_TEMPLATE_ADMIN_COURSE_FULL,Map.of("courseId", this.courseId));
            throw new CursoNaoPossueVagasException();
        } else {
            String reservationId = courseRepository.createReservation(nomeEstudante, emailEstudante,
                courseId, String.valueOf(LocalDate.now()));
            this.numberOfSeats--;
            courseRepository.updateNumberOfSeats(this.courseId, this.numberOfSeats);
            sendReservationEmail(nomeEstudante, emailEstudante);
            return reservationId;
        }
    }

  private void sendReservationEmail(String nomeEstudante, String email) {
    Map<String, Object> templateContext = Map.of(
        "name", nomeEstudante,
        "email", email,
        "courseId", courseId
    );
      final String mailSubject = "Reserva Curso";
      emailSender.sendEmail(email, mailSubject, VELOCITY_TEMPLATE_REPLY, templateContext);

    // Copy to the administrator
    emailSender.sendEmailToAdmin(mailSubject, VELOCITY_TEMPLATE_ADMIN_COPY, templateContext);
  }

  public int getNumberOfSeats(String courseId) {
        return mapCourses.get(courseId).getNumberOfSeats();
    }

    public String getDataInicio(String courseId) {
        return mapCourses.get(courseId).getDataInicio();
    }

    public Reservation getReservationByReservationId(String reservationId) {
        String sql = String.format("SELECT id, studentName, studentEmail, courseId, reservationDate FROM TBL_RESERVATIONS WHERE id = '%s'", reservationId);
        // max 1

        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
        ) {
            if (rs.next()) {
                Reservation reservation = new Reservation();
                String responseId = String.valueOf(rs.getInt("id"));
                reservation = new Reservation(rs.getString("studentEmail"), rs.getString("studentName"));
                reservation.setId(responseId);

                LOG.debug("ID: " + rs.getInt("id"));
                LOG.debug(", studentEmail: " + rs.getString("studentEmail"));
                LOG.debug(", studentName: " + rs.getString("studentName"));
                return reservation;
            } else {
                throw new IllegalArgumentException("reservation not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Getting reservation failed: ", e);
        }
    }

}
