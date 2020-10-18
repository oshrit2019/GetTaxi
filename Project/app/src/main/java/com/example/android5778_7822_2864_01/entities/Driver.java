package com.example.android5778_7822_2864_01.entities;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Driver
{
    String firstName;
    String lastName;
    int id;
    int phone;
    String email;
    int creditCard;

    /**
     * constractor
     * @param firstName
     * @param lastName
     * @param id
     * @param phone
     * @param email
     * @param creditCard
     */
    public Driver(String firstName, String lastName, int id, int phone, String email, int creditCard) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.creditCard = creditCard;
    }

    /**
     *defult constractor
     */
    public Driver() {
        this.firstName = "";
        this.lastName = "";
        this.id = 0;
        this.phone = 0;
        this.email = "";
        this.creditCard = 0;

    }

    /**
     * getter first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter Id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * setter Id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter phone
     * @return phone
     */
    public int getPhone() {
        return phone;
    }

    /**
     * setter phone
     * @param phone
     */
    public void setPhone(int phone) {
        this.phone = phone;
    }

    /**
     * getter email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter credit card
     * @return credit card
     */
    public int getCreditCard() {
        return creditCard;
    }

    /**
     * setter credit card
     * @param creditCard
     */
    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }

    /**
     * equals between two object kind from driver
     * @param o
     * @return true or false
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return id == driver.id &&
                phone == driver.phone &&
                creditCard == driver.creditCard &&
                Objects.equals(firstName, driver.firstName) &&
                Objects.equals(lastName, driver.lastName) &&
                Objects.equals(email, driver.email);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {

        return Objects.hash(firstName, lastName, id, phone, email, creditCard);
    }

    /**
     * to string
     * @return string
     */
    @Override
    public String toString() {
        return "Driver{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", phone=" + phone +
                ", email='" + email + '\'' +
                ", creditCard=" + creditCard +
                '}';
    }
}
