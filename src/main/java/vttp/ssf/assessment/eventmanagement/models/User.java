package vttp.ssf.assessment.eventmanagement.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {

    @NotNull(message = "Name is required")
    @Size(min = 5, max = 25, message = "Name must be between 5 to 25 characters long")
    private String fullName;

    @PastOrPresent(message = "Birth date must be in the past or present")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Match input date format
    private Date birthDate;

    @NotNull(message = "Email is required")
    @Email
    @Size(max = 50, message = "Maximum 50 characters")
    private String email;

    @Pattern(regexp = "(8|9)[0-9]{7}", message = "Phone number must start with 8 or 9 follow by 7 digits")
    private String phoneNumber;

    @NotNull(message = "Ticket amount is required")
    @Min(value = 1, message = "At least one ticket to request")
    @Max(value = 3, message = "Maximum of 3 tickets to request")
    private Integer ticketAmount;

    public User() {
    }

    public User(String fullName, Date birthDate, String email, String phoneNumber, Integer ticketAmount) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ticketAmount = ticketAmount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getTicketAmount() {
        return ticketAmount;
    }

    public void setTicketAmount(Integer ticketAmount) {
        this.ticketAmount = ticketAmount;
    }

    @Override
    public String toString() {
        return fullName + "," + birthDate + "," + email + ","
                + phoneNumber + "," + ticketAmount;
    }

}
