package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String homeAddress;

    public User(UUID id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public UUID getId() { return this.id; }
    public String getName() { return this.name; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public LocalDate getBirthDate() { return this.birthDate; }
    public String getEmail() {return this.email; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getHomeAddress() { return this.homeAddress; }

    public int getAge(){ return LocalDate.now().getYear() - this.birthDate.getYear(); }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setUsername(String username) {this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setHomeAddress(String homeAddress) { this.homeAddress = homeAddress; }

}
