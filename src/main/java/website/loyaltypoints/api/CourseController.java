package website.loyaltypoints.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import website.loyaltypoints.service.Course;
import website.loyaltypoints.service.CourseManager;
import website.loyaltypoints.service.Reservation;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Value("${app.version}")
    String appVersion;

    static CourseManager courseManager = new CourseManager();

    @PostMapping
    @RequestMapping("/create")
    public int createCourse(@RequestBody CourseDTO course) {
        return courseManager.createCourse(course.name, course.date, course.numberOfSeats);
    }

    @GetMapping("/{courseId}")
    public Course getCourse(@PathVariable("courseId") int courseId) {
        return courseManager.getCourse(courseId);
    }

    @PostMapping
    @RequestMapping("/reserve")
    public NewReservationResponseDTO createReservation(@RequestBody NewReservationRequestDTO reservation) throws Exception {

        String reservationId = courseManager.createReservation(reservation.courseId, reservation.studentName, reservation.studentEmail);

        return new NewReservationResponseDTO(reservationId);
    }

    @GetMapping("/reservation/{reservationId}")
    public Reservation getReservation(@PathVariable("reservationId") String reservationId) {
        return courseManager.getReservationByReservationId(reservationId);
    }
}
