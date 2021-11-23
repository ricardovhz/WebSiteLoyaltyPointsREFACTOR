package website.loyaltypoints.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CourseRepository {
  private static final Logger LOG = LoggerFactory.getLogger(CourseRepository.class);
  private String DB_URL = "jdbc:h2:mem:testdbs";
  private String USER = "sa";
  private String PASS = "";

  public String createReservation(
      String studentName, String studentEmail, int courseId, String reservationDate) {

    String sql =
        "INSERT INTO TBL_RESERVATIONS(studentName,studentEmail,courseId,reservationDate) VALUES (?, ?, ?, ?)";
    String reservationId = "";
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        PreparedStatement statement =
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); ) {

      LOG.debug("Inserting records into the table...");
      statement.setString(1, studentName);
      statement.setString(2, studentEmail);
      statement.setInt(3, courseId);
      statement.setString(4, reservationDate);
      statement.executeUpdate();

      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          reservationId = String.valueOf(generatedKeys.getLong(1));
        } else {
          throw new RuntimeException("Creating reservation failed, no ID obtained.");
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException("Creating reservation failed, conection failed", e);
    }

    return reservationId;
  }

  public void updateNumberOfSeats(int courseId, int numberOfSeats) {
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement stmt = connection.createStatement(); ) {
      String sqlUpdate =
          String.format(
              "UPDATE TBL_COURSES SET numberOfSeats = '%s' WHERE ID = '%s'",
              numberOfSeats, courseId);
      stmt.executeUpdate(sqlUpdate);
    } catch (SQLException e) {
      throw new RuntimeException("Creating reservation failed, conection failed", e);
    }
  }
}
