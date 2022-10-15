package com.lolafelingcatering.application.data.entity;

import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Entity
public class Reservations extends AbstractEntity {

    @Nonnull
    private String firstName;
    @Nonnull
    private String lastName;
    @Nonnull
    private String phoneNo;
    private LocalDateTime preferedSchedule;
    @Email
    @Nonnull
    private String email;
    @Nonnull
    private String address;
    @Nonnull
    private String package_;
    @Nonnull
    private String paymentStatus;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public LocalDateTime getPreferedSchedule() {
        return preferedSchedule;
    }
    public void setPreferedSchedule(LocalDateTime preferedSchedule) {
        this.preferedSchedule = preferedSchedule;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPackage_() {
        return package_;
    }
    public void setPackage_(String package_) {
        this.package_ = package_;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
