package com.example.storyweave.ui.profile;

import java.util.Date;

public class Profile {
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String email;

    public Profile(String firstname, String lastname, Date birthdate, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                ", email='" + email + '\'' +
                '}';
    }
}
