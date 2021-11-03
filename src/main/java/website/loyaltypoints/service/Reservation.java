package website.loyaltypoints.service;

public class Reservation {

    public String studentName;
    public String studentEmail;
    public String id;

    public Reservation() {
    }

    public Reservation(String studentName, String studentEmail) {
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        id = "";
    }

    public void setId(String id){
        this.id = id;
    }
}
