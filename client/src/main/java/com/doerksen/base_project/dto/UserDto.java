package com.doerksen.base_project.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * This is lombokified code. By simply annotating the class with @Data, we are able to generate all of the boilerplate
 * functions required for POJOs without actually coding anything. Compare it's size and simplicity to the block comment below.
 *
 * More information on Lombok can be found here: https://projectlombok.org/
 */

@AllArgsConstructor         // creates a constructor that includes all fields listed below
@Getter                     // creates getters for each of the fields below, if not all fields should be "gettable",
                            //   then individual fields can be annotated, the same goes for setters
@EqualsAndHashCode          // generates the equals and hash code methods as commented out in the block below
@ToString                   // generates the toString method as commented out in the block below
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
}


/**

 NOTE: This is the original, non-lombokified code (but was generated). See how much effort is required to implement the standard methods required?

public class UserDto {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String emailAddress;

    public UserDto(long id, String firstName, String lastName, String emailAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        if (id != userDto.id) return false;
        if (!firstName.equals(userDto.firstName)) return false;
        if (!lastName.equals(userDto.lastName)) return false;
        return emailAddress.equals(userDto.emailAddress);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ id >>> 32);
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + emailAddress.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}*/
