package website.loyaltypoints.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewReservationRequestDTO {

    public String studentName;
    public String studentEmail;
    public String courseId;

    public NewReservationRequestDTO (){}

    public NewReservationRequestDTO(String courseId, String studentName, String studentEmail) {
        this.courseId = courseId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
    }

}
