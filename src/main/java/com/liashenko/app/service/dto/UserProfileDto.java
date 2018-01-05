package com.liashenko.app.service.dto;

import java.io.Serializable;
import java.util.Arrays;

public class UserProfileDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private char[] pass;
    private char[] oldPass;
    private char[] repeatedPass;
    private String language;

    public UserProfileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPass() {
        return pass;
    }

    public void setPass(char[] pass) {
        this.pass = pass;
    }

    public char[] getOldPass() {
        return oldPass;
    }

    public void setOldPass(char[] oldPass) {
        this.oldPass = oldPass;
    }

    public char[] getRepeatedPass() {
        return repeatedPass;
    }

    public void setRepeatedPass(char[] repeatedPass) {
        this.repeatedPass = repeatedPass;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileDto)) return false;

        UserProfileDto that = (UserProfileDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (!Arrays.equals(pass, that.pass)) return false;
        if (!Arrays.equals(oldPass, that.oldPass)) return false;
        if (!Arrays.equals(repeatedPass, that.repeatedPass)) return false;
        return language != null ? language.equals(that.language) : that.language == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(pass);
        result = 31 * result + Arrays.hashCode(oldPass);
        result = 31 * result + Arrays.hashCode(repeatedPass);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserProfileDto{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", pass=").append(Arrays.toString(pass));
        sb.append(", oldPass=").append(Arrays.toString(oldPass));
        sb.append(", repeatedPass=").append(Arrays.toString(repeatedPass));
        sb.append(", language='").append(language).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
