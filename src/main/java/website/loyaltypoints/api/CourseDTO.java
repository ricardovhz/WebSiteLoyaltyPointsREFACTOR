package website.loyaltypoints.api;

public class CourseDTO {

    public String name;
    public String date;
    public int numberOfSeats;

    public CourseDTO() {
    }

    public CourseDTO(String name, String date, int numberOfSeats) {
        this.name = name;
        this.date = date;
        this.numberOfSeats = numberOfSeats;
    }
}
