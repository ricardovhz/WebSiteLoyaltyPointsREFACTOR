package website.loyaltypoints.api;

import org.springframework.web.bind.annotation.*;
import website.loyaltypoints.service.Course;
import website.loyaltypoints.service.CourseManager;
import website.loyaltypoints.service.Reservation;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    static CourseManager courseManager = new CourseManager();

    @PostMapping
    @RequestMapping("/create")
    public void createCourse(@RequestBody CourseDTO course) {
        courseManager.createCourse(course.courseId, course.name, course.date, course.numberOfSeats);
    }

    @GetMapping("/{courseId}")
    public Course getCourse(@PathVariable("courseId") String courseId) {
        return courseManager.getCourse(courseId);
    }

    @PostMapping
    @RequestMapping("/reserve")
    public NewReservationResponseDTO createReservation(@RequestBody NewReservationRequestDTO reservation) {
        String reservationId = courseManager.createReservation(reservation.courseId, reservation.studentName, reservation.studentEmail);

        return new NewReservationResponseDTO(reservationId);
    }

    @GetMapping("/reservation/{courseId}/{reservationId}")
    public Reservation getReservation(@PathVariable("courseId") String courseId, @PathVariable("reservationId") String reservationId) {
        return courseManager.getReservationByCourseAndReservationId(courseId, reservationId);
    }
}
