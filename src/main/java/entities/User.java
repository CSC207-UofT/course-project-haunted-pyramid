package entities;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private Date birthDay;
    private String email;
    private String phoneNumber;
    private String homeAddress;


    public User(String name, Date birthDay, int id) {
        this.name = name;
        this.birthDay = birthDay;
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public int getAge(){
        Date date = new Date();
        return date.getYear() - this.birthDay.getYear();
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }
}
