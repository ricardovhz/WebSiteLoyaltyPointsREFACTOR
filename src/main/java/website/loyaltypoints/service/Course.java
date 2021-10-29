package website.loyaltypoints.service;

import java.util.ArrayList;
import java.util.List;

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
