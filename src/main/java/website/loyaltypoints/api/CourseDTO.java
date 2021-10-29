package website.loyaltypoints.api;

public class CourseDTO {

    public String courseId;
    public String name;
    public String date;
    public int numberOfSeats;

    public CourseDTO() {
    }

    public CourseDTO(String courseId, String name, String date, int numberOfSeats) {
        this.courseId = courseId;
        this.name = name;
        this.date = date;
        this.numberOfSeats = numberOfSeats;
    }
}
