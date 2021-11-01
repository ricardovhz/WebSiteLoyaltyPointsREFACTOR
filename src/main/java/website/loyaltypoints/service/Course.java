package website.loyaltypoints.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Course {

    String courseId;
    String name;
    String date;
    int numberOfSeats;

    List<Reservation> reservations;

    public Course() {
    }

    public Course(String courseId, String name, String date, int numberOfSeats) {
        this.courseId = courseId;
        this.name = name;
        this.date = date;
        this.numberOfSeats = numberOfSeats;
        reservations = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return name;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public String createReservation(String nomeEstudante, String emailEstudante) {
        Reservation novaReservaVaga = new Reservation(nomeEstudante, emailEstudante);
        reservations.add(novaReservaVaga);
        numberOfSeats--;

        String DB_URL = "jdbc:h2:mem:testdbs://localhost:8080/h2";
        String USER = "sa";
        String PASS = "";

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
        ) {
            // Execute a query
            System.out.println("Inserting records into the table...");
            String sql = String.format("INSERT INTO TBL_RESERVATIONS(studentName,studentEmail) VALUES ('%s', '%s')", nomeEstudante, emailEstudante);
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return novaReservaVaga.id;
    }

    public Reservation getReservation(String reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.id.equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    public String getDataInicio() {
        return date;
    }
}
