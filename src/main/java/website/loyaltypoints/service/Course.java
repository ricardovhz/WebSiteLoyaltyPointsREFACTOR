package website.loyaltypoints.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Course {

    int courseId;
    String name;
    String date;
    int numberOfSeats;

    List<Reservation> reservations;

    public Course() {
    }

    public Course(String name, String date, int numberOfSeats) {
        this.name = name;
        this.date = date;
        this.numberOfSeats = numberOfSeats;
        reservations = new ArrayList<>();
    }

    public int getCourseId() {
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
        novaReservaVaga.setId(String.valueOf(reservations.size()));
        reservations.add(novaReservaVaga);
        numberOfSeats--;

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

    public void setId(int courseId) {
        this.courseId = courseId;
    }
}
